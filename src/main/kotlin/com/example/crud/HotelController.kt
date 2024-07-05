package com.example.crud

import com.example.model.Hotel
import com.example.repository.DestinationRepository
import com.example.repository.HotelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController

class HotelController {
    @Autowired
    lateinit var hotelRepository: HotelRepository
    @Autowired
    lateinit var destinationRepository: DestinationRepository

    @GetMapping("/hotels")
    fun getAll(): List<Hotel> {
        return hotelRepository.findAll()
    }

    @PostMapping("/hotels")
    fun create(@RequestBody hotel: Hotel): ResponseEntity<Hotel> {
        // Guardo la instancia de Destination primero
        var destination = hotel.destination
        if (destination != null && destination.id == null) {
            destination = destinationRepository.save(destination)
        }

        // Asigno la instancia guardada de Destination al Hotel
        hotel.destination = destination

        // Guardo la instancia de Hotel
        val savedHotel = hotelRepository.save(hotel)
        return ResponseEntity.ok(savedHotel)
    }
}