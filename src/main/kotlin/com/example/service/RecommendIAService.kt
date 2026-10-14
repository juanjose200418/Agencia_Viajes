package com.example.service

import com.example.model.Destination
import com.example.model.RecommendationSearchResponse
import com.example.repository.DestinationRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Service
class RecommendationService(
    private val destinationRepository: DestinationRepository
) {

    @Value("\${OPENAI_API_KEY}")
    lateinit var openAiApiKey: String

    private val objectMapper = jacksonObjectMapper()

    // RestClient síncrono para llamar a OpenAI
    private val restClient: RestClient by lazy {
        RestClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $openAiApiKey")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

    // Caché simple: userQuery -> Pair(respuesta, timestamp)
    private val cache = ConcurrentHashMap<String, Pair<String, Instant>>()
    private val cacheDuration = Duration.ofMinutes(5)
    @Volatile
    private var destinationsCache: Pair<List<Destination>, Instant>? = null

    fun getDestinationRecommendations(userQuery: String): String {
        // 1️⃣ Revisar caché
        cache[userQuery]?.let { (cachedResponse, timestamp) ->
            if (Instant.now().isBefore(timestamp.plus(cacheDuration))) {
                return cachedResponse
            } else {
                cache.remove(userQuery) // Expiró la caché
            }
        }

        // 2️⃣ Obtener destinos desde la base de datos
        val destinations: List<Destination> = getActiveDestinations()
        if (destinations.isEmpty()) return "No hay destinos disponibles"

        // 3️⃣ Serializar destinos a JSON
        val destinationsJson = objectMapper.writeValueAsString(destinations)

        // 4️⃣ Construir prompt para OpenAI
        val prompt = """
            Eres un asistente de viajes.
            Estos son los destinos disponibles (formato JSON):
            $destinationsJson

            Preferencia del usuario: "$userQuery"
            Recomienda 3 destinos y explica por qué en español.
        """.trimIndent()

        // 5️⃣ Llamar a OpenAI y guardar en caché
        val responseMessage = callOpenAi(prompt)
        cache[userQuery] = Pair(responseMessage, Instant.now())
        return responseMessage
    }

    /**
     * Búsqueda inteligente: pide a OpenAI que seleccione los destinos más
     * adecuados para la consulta y devuelve SOLO destinos reales de la base
     * de datos (para evitar alucinaciones) junto con una explicación en español.
     */
    fun searchDestinations(userQuery: String): RecommendationSearchResponse {
        val queryKey = "search:$userQuery"
        cache[queryKey]?.let { (cachedJson, timestamp) ->
            if (Instant.now().isBefore(timestamp.plus(cacheDuration))) {
                parseSearchResponse(cachedJson)?.let { return it }
            } else {
                cache.remove(queryKey)
            }
        }

        val destinations = getActiveDestinations()
        val empty = RecommendationSearchResponse(emptyList(), "No hay destinos disponibles")

        // Si no hay búsqueda, devolvemos todos los destinos como fallback local
        if (userQuery.isBlank()) {
            return RecommendationSearchResponse(destinations, "Mostrando todos los destinos disponibles.")
        }

        val catalog = destinations.map { dest ->
            mapOf(
                "name" to (dest.name ?: ""),
                "country" to (dest.country ?: ""),
                "pointsOfInterest" to dest.pointsOfInterest,
                "averageCost" to (dest.averageCost ?: 0)
            )
        }
        val catalogJson = objectMapper.writeValueAsString(catalog)

        val prompt = """
            Eres un buscador de viajes.
            El usuario busca destinos con esta consulta: "$userQuery"
            Catálogo disponible de destinos (JSON):
            $catalogJson

            Responde ÚNICAMENTE con JSON válido (sin markdown) con esta forma exacta:
            {"destinations": ["NombreDestino1", "NombreDestino2", ...], "explanation": "Explicación breve en español de por qué se recomiendan."}
            Reglas:
            - Elige como máximo 5 destinos del catálogo que mejor encajen con la consulta.
            - Usa EXACTAMENTE el valor del campo "name" de cada destino.
            - Si ningún destino encaja bien, devuelve destinations vacío y una explicación útil.
            - La explicación debe ser de 1 a 3 frases en español.
        """.trimIndent()

        try {
            val raw = callOpenAi(prompt)
            cache[queryKey] = Pair(raw, Instant.now())
            return parseSearchResponse(raw, destinations) ?: empty
        } catch (e: Exception) {
            // Fallback local: filtrado por coincidencia en nombre/país/atracciones
            return localSearch(userQuery, destinations)
        }
    }

    private fun parseSearchResponse(raw: String, allDestinations: List<Destination> = getActiveDestinations()): RecommendationSearchResponse? {
        return try {
            val json = extractJson(raw)
            val root = objectMapper.readTree(json)
            val names = root.path("destinations").mapNotNull { it.asText() }
            val explanation = root.path("explanation").asText("")
            val byName = allDestinations.associateBy { (it.name ?: "").trim().lowercase() }
            val matched = names.mapNotNull { n ->
                byName[n.trim().lowercase()]
            }.distinctBy { it.id }.take(5)
            RecommendationSearchResponse(matched, explanation)
        } catch (e: Exception) {
            null
        }
    }

    private fun extractJson(raw: String): String {
        val start = raw.indexOf('{')
        val end = raw.lastIndexOf('}')
        return if (start >= 0 && end > start) raw.substring(start, end + 1) else raw
    }

    private fun localSearch(userQuery: String, destinations: List<Destination>): RecommendationSearchResponse {
        val q = userQuery.lowercase().trim()
        val words = q.split(Regex("[^a-z0-9áéíóúñü]+")).filter { it.length > 1 }
        val scored = destinations.mapNotNull { dest ->
            val haystack = listOf(
                dest.name, dest.country, dest.location, dest.pointsOfInterest.joinToString(" "), dest.description
            ).filterNotNull().joinToString(" ").lowercase()
            val score = words.count { haystack.contains(it) }
            if (score > 0) score to dest else null
        }.sortedWith(compareByDescending<Pair<Int, Destination>> { it.first }.thenBy { it.second.name ?: "" })
        val matched = scored.map { it.second }.take(5)
        return RecommendationSearchResponse(matched, "No pude contactar con la IA; estos son los destinos que coinciden con tu búsqueda.")
    }

    private fun getActiveDestinations(): List<Destination> {
        destinationsCache?.let { (cached, timestamp) ->
            if (Instant.now().isBefore(timestamp.plus(cacheDuration))) return cached
        }
        val fresh = destinationRepository.findAllByDeleted(false)
        destinationsCache = Pair(fresh, Instant.now())
        return fresh
    }

    private fun callOpenAi(prompt: String): String {
        val requestBody = mapOf(
            "model" to "gpt-4o-mini",
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to prompt
                )
            ),
            "temperature" to 0.7
        )

        var attempts = 0
        val maxAttempts = 3
        var responseMessage: String

        while (true) {
            try {
                val rawBody = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map::class.java)
                    ?: throw IllegalStateException("Respuesta vacía de OpenAI")

                @Suppress("UNCHECKED_CAST")
                val response = rawBody as Map<String, Any?>

                val choices = response["choices"] as List<*>
                val message = (choices[0] as Map<*, *>)["message"] as Map<*, *>
                responseMessage = message["content"].toString()
                break
            } catch (e: Exception) {
                if (e.message?.contains("429") == true && attempts < maxAttempts) {
                    attempts++
                    Thread.sleep(1000L * attempts) // espera creciente: 1s, 2s, 3s
                } else {
                    throw e
                }
            }
        }

        return responseMessage
    }
}
