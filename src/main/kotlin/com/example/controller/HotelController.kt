package com.example.controller

import com.example.model.Hotel
import com.example.service.HotelService
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
class HotelController {

    @Autowired
    lateinit var hotelService: HotelService

    @GetMapping("/hotels")
    fun getAll(): List<Hotel> {
        return hotelService.getAll()
    }

    @GetMapping("/hotels", params = ["page", "size"])
    fun getPage(@RequestParam page: Int, @RequestParam size: Int): Page<Hotel> {
        return hotelService.getPage(page, size)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/hotels")
    fun create(@RequestBody hotel: Hotel): Hotel {
        return hotelService.create(hotel)
    }

    @GetMapping("/hotels/{id}")
    fun getById(@PathVariable id: Long): Hotel {
        return hotelService.getById(id)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/hotels/{id}")
    fun deleteById(@PathVariable id: Long) {
        hotelService.deleteById(id)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/hotels/{id}")
    fun update(@PathVariable id: Long, @RequestBody hotel: Hotel): Hotel {
        return hotelService.update(id, hotel)
    }

    @GetMapping("/destination/{destinationId}/hotels")
    fun getByDestinationId(@PathVariable destinationId: Long): List<Hotel> {
        return hotelService.getAllByDestinationId(destinationId)
    }
}
