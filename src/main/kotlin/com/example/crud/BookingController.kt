package com.example.crud

import com.example.model.Booking
import com.example.repository.BookingRepository
import com.example.service.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BookingController {
    @Autowired
    lateinit var bookingService: BookingService

    @GetMapping("/bookings")
    fun getAll(): List<Booking> {
        return bookingService.getAll()
    }

    @PostMapping("/bookings")
    fun create(@RequestBody booking: Booking): Booking {
        return bookingService.create(booking)
    }

    @GetMapping("/bookings/{id}")
    fun getById(@PathVariable id: Long): Booking {
        return bookingService.getById(id)
    }
}
