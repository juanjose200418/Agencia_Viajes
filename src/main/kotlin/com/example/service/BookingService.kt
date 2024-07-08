package com.example.service

import com.example.model.Booking
import com.example.repository.BookingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookingService {
    @Autowired
    lateinit var bookingRepository: BookingRepository

    fun getAll():List<Booking>{
        return bookingRepository.findAll()
    }

    fun create(booking: Booking): Booking {
        return bookingRepository.save(booking)
    }
}
