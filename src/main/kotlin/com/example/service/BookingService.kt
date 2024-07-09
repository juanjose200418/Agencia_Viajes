package com.example.service

import com.example.exception.ResourceNotFoundException
import com.example.model.Booking
import com.example.repository.BookingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookingService {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var flightService: FlightService

    @Autowired
    lateinit var bookingRepository: BookingRepository

    @Autowired
    lateinit var destinationService: DestinationService

    @Autowired
    lateinit var hotelService: HotelService

    fun getAll(): List<Booking> {
        return bookingRepository.findAll()
    }

    fun create(booking: Booking): Booking {
        if (!hotelService.existsById(booking.hotel!!.id!!)) {
            throw ResourceNotFoundException("Hotel with id ${booking.hotel!!.id!!} does not exist")
        }
        if (!userService.existsById(booking.user!!.id!!)) {
            throw ResourceNotFoundException("User with id ${booking.user!!.id!!} does not exist")
        }
        if (!flightService.existsById(booking.flight!!.id!!)) {
            throw ResourceNotFoundException("Flight with id ${booking.flight!!.id!!} does not exist")
        }
        if (!destinationService.existsById(booking.destination!!.id!!)) {
            throw ResourceNotFoundException("Destination with id ${booking.destination!!.id!!} does not exist")
        }

        return bookingRepository.save(booking)
    }

    fun getById(id: Long): Booking {
        return bookingRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Booking with id $id does not exist")
        }
    }

    fun deleteById(id: Long) {
        if (!bookingRepository.existsById(id)) {
            throw ResourceNotFoundException("Booking with id $id does not exist")
        }
        bookingRepository.deleteById(id)
    }
}
