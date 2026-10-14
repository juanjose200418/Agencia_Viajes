package com.example.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.text.Normalizer
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs

@Service
class AutoImageService {
    private val objectMapper = jacksonObjectMapper()
    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build()
    private val wikipediaCache = ConcurrentHashMap<String, String?>()

    fun destinationImageUrl(name: String?, country: String?, width: Int = 800, height: Int = 600): String {
        val title = destinationWikipediaTitle(name, country)
        wikipediaImage(title, width)?.let { return it }

        val query = listOfNotNull(name, country, "travel", "landmark")
            .joinToString(" ")
            .toTags()
        return imageUrl(width, height, query, name ?: country ?: "destination")
    }

    fun hotelImageUrl(hotelName: String?, destinationName: String?, width: Int = 800, height: Int = 600): String {
        wikipediaImage(hotelName, width)?.let { return it }
        return hotelFallbackImage(hotelName ?: destinationName ?: "hotel")
    }

    private fun wikipediaImage(title: String?, width: Int): String? {
        if (title.isNullOrBlank()) return null
        val cacheKey = "${title.trim().lowercase()}:$width"
        if (wikipediaCache.containsKey(cacheKey)) return wikipediaCache[cacheKey]
        val encoded = URLEncoder.encode(title.trim(), StandardCharsets.UTF_8).replace("+", "%20")
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://en.wikipedia.org/api/rest_v1/page/summary/$encoded"))
            .timeout(Duration.ofSeconds(8))
            .header("User-Agent", "AgenciaViajes/1.0 (local development)")
            .GET()
            .build()

        val result = runCatching {
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() !in 200..299) return@runCatching null
            val root = objectMapper.readTree(response.body())
            val original = root.path("originalimage").path("source").asText(null)
            val thumbnail = root.path("thumbnail").path("source").asText(null)
            cleanWikimediaUrl(original) ?: cleanWikimediaUrl(thumbnail)
        }.getOrNull()

        wikipediaCache[cacheKey] = result
        return result
    }

    private fun cleanWikimediaUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return url.substringBefore("?utm_source=")
    }

    private fun destinationWikipediaTitle(name: String?, country: String?): String? {
        val normalized = (name ?: "").toSlug()
        val overrides = mapOf(
            "reunion-island" to "Réunion",
            "reykjavik-south" to "Iceland",
            "dubai-marina" to "Dubai Marina",
            "ciudad-de-mexico" to "Mexico City",
            "pekin" to "Beijing",
            "sidney" to "Sydney",
            "sao-paulo" to "São Paulo",
            "rio-de-janeiro" to "Rio de Janeiro",
            "male" to "Malé",
            "cartagena" to "Cartagena, Colombia",
            "hanoi" to "Hanoi",
            "shanghai" to "Shanghai",
            "dubrovnik" to "Dubrovnik",
            "kingston" to "Kingston, Jamaica",
            "berlin" to "Berlin",
            "paris" to "Paris",
            "santiago" to "Santiago",
            "london" to "London",
            "rome" to "Rome",
            "munich" to "Munich"
        )
        return overrides[normalized] ?: name ?: country
    }

    private fun hotelFallbackImage(seed: String): String {
        val images = listOf(
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=900&q=80",
            "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=900&q=80",
            "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=900&q=80",
            "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=900&q=80",
            "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=900&q=80",
            "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=900&q=80"
        )
        return images[abs(seed.toSlug().hashCode()) % images.size]
    }

    private fun imageUrl(width: Int, height: Int, tags: String, seed: String): String {
        val lock = abs(seed.toSlug().hashCode())
        return "https://loremflickr.com/$width/$height/$tags?lock=$lock"
    }

    private fun String.toTags(): String = toSlug().replace("-", ",")

    private fun String.toSlug(): String {
        val normalized = Normalizer.normalize(lowercase(), Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
            .replace("ñ", "n")
        return normalized.replace(Regex("[^a-z0-9]+"), "-").trim('-')
    }
}
