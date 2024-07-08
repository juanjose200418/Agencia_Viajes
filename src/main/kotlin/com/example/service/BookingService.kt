package com.example.service

import com.example.exception.ResourceNotFoundException
import com.example.model.Booking
import com.example.repository.BookingRepository
import com.example.repository.HotelRepository
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
    fun getById(id: Long): Booking {
        return bookingRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Booking with id $id does not exist")
        }
    }
}
