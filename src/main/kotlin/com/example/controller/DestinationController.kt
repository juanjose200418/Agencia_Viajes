package com.example.controller

import com.example.model.Destination
import com.example.service.DestinationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DestinationController {

    @Autowired
    lateinit var destinationService: DestinationService

    @GetMapping("/destinations")
    fun getAll(): List<Destination> {
        return destinationService.getAll()
    }

    @GetMapping("/destinations", params = ["page", "size"])
    fun getPage(
        @RequestParam page: Int,
        @RequestParam size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) country: String?,
        @RequestParam(required = false) location: String?,
        @RequestParam(required = false) hasImage: Boolean?,
        @RequestParam(required = false) minAverageCost: Double?,
        @RequestParam(required = false) maxAverageCost: Double?,
        @RequestParam(required = false) hotelPresence: String?,
        @RequestParam(required = false) sortBy: String?,
    ): Page<Destination> {
        return destinationService.getPage(page, size, search, country, location, hasImage, minAverageCost, maxAverageCost, hotelPresence, sortBy)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/destinations/filter-options")
    fun getFilterOptions(): Map<String, List<String>> {
        return destinationService.getFilterOptions()
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/destinations")
    fun create(@RequestBody destination: Destination): Destination {
        return destinationService.create(destination)
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/destinations/bulk")
    fun createDestinations(@RequestBody destinations: List<Destination>): List<Destination> {
        return destinationService.createAll(destinations)
    }

    @GetMapping("/destinations/{id}")
    fun getById(@PathVariable id: Long): Destination {
        return destinationService.getById(id)
    }

    @GetMapping("/destinations/slug/{slug}")
    fun getBySlug(@PathVariable slug: String): Destination {
        return destinationService.getBySlug(slug)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/destinations/{id}")
    fun deleteById(@PathVariable id: Long) {
        return destinationService.deleteById(id)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("destinations/{id}")
    fun update(@PathVariable id: Long, @RequestBody destination: Destination): Destination {
        return destinationService.update(id, destination)

    }
}
