package com.example.service

import com.example.exception.AnotherException
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
        return bookingRepository.findAllByDeleted(false)
    }

    fun create(booking: Booking): Booking {
        if (!hotelService.existsById(booking.hotelId!!)) {
            throw ResourceNotFoundException("Hotel with id ${booking.hotelId} does not exist")
        }
        if (!userService.existsById(booking.userId!!)) {
            throw ResourceNotFoundException("User with id ${booking.userId} does not exist")
        }
        if (!flightService.existsById(booking.flightId!!)) {
            throw ResourceNotFoundException("Flight with id ${booking.flightId} does not exist")
        }
        if (!destinationService.existsById(booking.destinationId!!)) {
            throw ResourceNotFoundException("Destination with id ${booking.destinationId} does not exist")
        }

        return bookingRepository.save(booking)
    }

    fun getById(id: Long): Booking {
        return bookingRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Booking with id $id does not exist")
        }
    }

    fun deleteById(id: Long) {
        checkExist(id)
        //Creacin del soft delete
        val booking = bookingRepository.getReferenceById(id)
        booking.deleted = true
        bookingRepository.save(booking)
    }

    private fun checkExist(id: Long) {
        if (!bookingRepository.existsById(id)) {
            throw ResourceNotFoundException("Booking with id $id does not exist")
        }
    }

    fun update(id: Long, booking: Booking): Booking {
        checkExist(id)
        if (booking.id != id) {
            throw AnotherException("Booking with id $id does not exist")
        }
        return bookingRepository.save(booking)
    }
}
