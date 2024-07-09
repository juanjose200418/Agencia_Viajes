package com.example.crud

import com.example.model.Hotel
import com.example.repository.DestinationRepository
import com.example.repository.HotelRepository
import com.example.service.HotelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HotelController {

    @Autowired
    lateinit var hotelService: HotelService

    @GetMapping("/hotels")
    fun getAll(): List<Hotel> {
        return hotelService.getAll()
    }

    @PostMapping("/hotels")
    fun create(@RequestBody hotel: Hotel): Hotel {
        return hotelService.create(hotel)
    }

    @GetMapping("/hotels/{id}")
    fun getById(@PathVariable id: Long): Hotel {
        return hotelService.getById(id)
    }
}