package com.example.controller

import com.example.model.Booking
import com.example.service.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN') or #id == authentication.principal.id")
    @DeleteMapping("bookings/{id}")
    fun deleteById(@PathVariable id: Long) {
        bookingService.deleteById(id)
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN') or #id == authentication.principal.id")
    @PutMapping("bookings/{id}")
    fun update(@PathVariable id: Long, @RequestBody booking: Booking): Booking {
        return bookingService.update(id, booking)
    }
}
