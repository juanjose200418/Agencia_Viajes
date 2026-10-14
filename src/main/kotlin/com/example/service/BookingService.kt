package com.example.service

import com.example.model.Booking
import com.example.model.Role
import com.example.model.User
import com.example.repository.BookingRepository
import com.example.security.SessionManager
import com.example.shared.exception.AnotherException
import com.example.shared.exception.ResourceNotFoundException
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

    @Autowired
    lateinit var sessionManager: SessionManager

    fun getAll(): List<Booking> {
        val all = bookingRepository.findAllByDeleted(false)
        val currentUser = sessionManager.getUserIdentity()
        if (_isAdmin(currentUser)) {
            return all
        }
        return all.filter { it.userId == currentUser.id }
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

        val currentUser = sessionManager.getUserIdentity()
        if (!_isAdmin(currentUser)) {
            booking.userId = currentUser.id
        }

        return bookingRepository.save(booking)
    }

    fun getById(id: Long): Booking {
        val booking = bookingRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Booking with id $id does not exist")
        }
        _checkOwnership(booking)
        return booking
    }

    fun deleteById(id: Long) {
        checkExist(id)
        val booking = bookingRepository.getReferenceById(id)
        _checkOwnership(booking)
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
        _checkOwnership(booking)
        val currentUser = sessionManager.getUserIdentity()
        if (!_isAdmin(currentUser)) {
            booking.userId = currentUser.id
        }
        return bookingRepository.save(booking)
    }

    private fun _checkOwnership(booking: Booking) {
        val currentUser = sessionManager.getUserIdentity()
        if (_isAdmin(currentUser)) {
            return
        }
        if (booking.userId != currentUser.id) {
            throw ResourceNotFoundException("Booking with id ${booking.id} does not exist")
        }
    }

    private fun _isAdmin(user: User): Boolean {
        return user.role == Role.ADMIN || user.role == Role.SUPER_ADMIN
    }
}
