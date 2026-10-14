package com.example.service

import com.example.shared.exception.AnotherException
import com.example.shared.exception.ResourceNotFoundException
import com.example.model.Destination
import com.example.model.Hotel
import com.example.repository.DestinationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.text.Normalizer

@Service
class DestinationService(
    private val autoImageService: AutoImageService
) {
    @Autowired
    lateinit var destinationRepository: DestinationRepository

    fun getAll(): List<Destination> {
        return destinationRepository.findAllByDeleted(false).map { withImageFallback(it) }
    }

    fun getPage(
        page: Int,
        size: Int,
        search: String? = null,
        country: String? = null,
        location: String? = null,
        hasImage: Boolean? = null,
        minAverageCost: Double? = null,
        maxAverageCost: Double? = null,
        hotelPresence: String? = null,
        sortBy: String? = null,
    ): Page<Destination> {
        return destinationRepository.findAll(
            destinationSpec(search, country, location, hasImage, minAverageCost, maxAverageCost, hotelPresence),
            PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 100), destinationSort(sortBy))
        ).map { withImageFallback(it) }
    }

    fun getFilterOptions(): Map<String, List<String>> {
        return mapOf(
            "countries" to destinationRepository.findDistinctCountries().map { it.trim() }.filter { it.isNotBlank() },
            "locations" to destinationRepository.findDistinctLocations().map { it.trim() }.filter { it.isNotBlank() },
        )
    }

    fun create(destination: Destination): Destination {
        destination.slug = destination.slug?.takeIf { it.isNotBlank() } ?: slugify(destination.name ?: "")
        destination.imageUrl = destination.imageUrl?.takeIf { it.isNotBlank() }
            ?: autoImageService.destinationImageUrl(destination.name, destination.country)
        destination.pointsOfInterest = normalizePoints(destination.pointsOfInterest).toMutableList()
        destination.popularAttractions = null
        destination.deleted = false
        return destinationRepository.save(destination)
    }
    fun createAll(destinations: List<Destination>): List<Destination> {
        return destinationRepository.saveAll(destinations)
    }

    fun getById(id: Long): Destination {
        return withImageFallback(destinationRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Destination with id $id does not exist")
        })
    }

    fun getBySlug(slug: String): Destination {
        val destination = destinationRepository.findBySlugAndDeleted(slug, false) ?: throw ResourceNotFoundException(
            "Destination with slug $slug does not exist"
        )
        return withImageFallback(destination)
    }

    fun existsById(id: Long): Boolean {
        return destinationRepository.existsById(id)
    }

    fun deleteById(id: Long) {
        checkExist(id)
        val destination = destinationRepository.getReferenceById(id)
        destination.deleted = true
        destinationRepository.save(destination)
    }

    private fun checkExist(id: Long) {
        if (!destinationRepository.existsById(id)) {
            throw ResourceNotFoundException("Destination with id $id does not exist")
        }
    }

    fun update(id: Long, destination: Destination): Destination {
        checkExist(id)
        if (destination.id != id) {
            throw AnotherException("Destination with id $id does not exist")
        }
        val existing = getById(id)
        existing.name = destination.name ?: existing.name
        existing.slug = destination.slug?.takeIf { it.isNotBlank() }
            ?: existing.slug
            ?: slugify(existing.name ?: "")
        existing.country = destination.country ?: existing.country
        existing.description = destination.description ?: existing.description
        existing.location = destination.location ?: existing.location
        existing.imageUrl = destination.imageUrl?.takeIf { it.isNotBlank() }
            ?: existing.imageUrl?.takeIf { it.isNotBlank() }
            ?: autoImageService.destinationImageUrl(existing.name, existing.country)
        existing.pointsOfInterest = normalizePoints(destination.pointsOfInterest).toMutableList()
        existing.popularAttractions = null
        existing.averageCost = destination.averageCost ?: existing.averageCost
        existing.deleted = destination.deleted ?: existing.deleted
        return destinationRepository.save(existing)
    }

    private fun slugify(value: String): String {
        val normalized = Normalizer.normalize(value.lowercase(), Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
            .replace("ñ", "n")
        return normalized
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
    }

    private fun withImageFallback(destination: Destination): Destination {
        if (destination.imageUrl.isNullOrBlank()) {
            destination.imageUrl = autoImageService.destinationImageUrl(destination.name, destination.country)
        }
        if (destination.pointsOfInterest.isEmpty() && !destination.popularAttractions.isNullOrBlank()) {
            destination.pointsOfInterest = splitLegacyPoints(destination.popularAttractions).toMutableList()
        }
        return destination
    }

    private fun normalizePoints(points: List<String>?): List<String> {
        return points.orEmpty().map { it.trim() }.filter { it.isNotBlank() }.distinct()
    }

    private fun splitLegacyPoints(value: String?): List<String> {
        return value.orEmpty()
            .split("\n", ",", ";", "|")
            .map { it.trim().trim('-', '•') }
            .filter { it.isNotBlank() }
            .distinct()
    }

    private fun destinationSort(sortBy: String?): Sort {
        return when (sortBy?.uppercase()) {
            "NAME_DESC" -> Sort.by("name").descending()
            "COUNTRY_ASC" -> Sort.by("country").ascending().and(Sort.by("name").ascending())
            "COST_ASC" -> Sort.by("averageCost").ascending()
            "COST_DESC" -> Sort.by("averageCost").descending()
            "CREATED_DESC" -> Sort.by("createdAt").descending()
            "CREATED_ASC" -> Sort.by("createdAt").ascending()
            else -> Sort.by("name").ascending()
        }
    }

    private fun destinationSpec(
        search: String?,
        country: String?,
        location: String?,
        hasImage: Boolean?,
        minAverageCost: Double?,
        maxAverageCost: Double?,
        hotelPresence: String?,
    ): Specification<Destination> {
        return Specification { root, query, cb ->
            query?.distinct(true)
            val predicates = mutableListOf(cb.isFalse(root.get("deleted")))

            search?.trim()?.takeIf { it.isNotBlank() }?.lowercase()?.let { term ->
                val pattern = "%$term%"
                predicates += cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("country")), pattern),
                    cb.like(cb.lower(root.get("location")), pattern),
                    cb.like(cb.lower(root.get("slug")), pattern),
                )
            }
            country?.trim()?.takeIf { it.isNotBlank() }?.let {
                predicates += cb.equal(cb.lower(root.get("country")), it.lowercase())
            }
            location?.trim()?.takeIf { it.isNotBlank() }?.let {
                predicates += cb.equal(cb.lower(root.get("location")), it.lowercase())
            }
            hasImage?.let { expected ->
                val imageExists = cb.and(cb.isNotNull(root.get<String>("imageUrl")), cb.greaterThan(cb.length(root.get("imageUrl")), 0))
                predicates += if (expected) imageExists else cb.not(imageExists)
            }
            minAverageCost?.let { predicates += cb.greaterThanOrEqualTo(root.get("averageCost"), it) }
            maxAverageCost?.let { predicates += cb.lessThanOrEqualTo(root.get("averageCost"), it) }

            hotelPresence?.uppercase()?.takeIf { it == "WITH_HOTELS" || it == "WITHOUT_HOTELS" }?.let { mode ->
                val subquery = query!!.subquery(Long::class.java)
                val hotel = subquery.from(Hotel::class.java)
                subquery.select(cb.literal(1L)).where(
                    cb.equal(hotel.get<Long>("destinationId"), root.get<Long>("id")),
                    cb.isFalse(hotel.get("deleted")),
                )
                predicates += if (mode == "WITH_HOTELS") cb.exists(subquery) else cb.not(cb.exists(subquery))
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}
