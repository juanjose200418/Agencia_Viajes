package com.example.service

import com.example.shared.exception.AnotherException
import com.example.shared.exception.ResourceNotFoundException
import com.example.model.Flight
import com.example.repository.FlightRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FlightService {
    @Autowired
    lateinit var flightRepository: FlightRepository

    fun getAll(): List<Flight> {
        return flightRepository.findAllByDeleted(false)
    }

    fun create(flight: Flight): Flight {
        return flightRepository.save(flight)
    }

    fun getById(id: Long): Flight {
        return flightRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Flight with id $id does not exist")
        }
    }

    fun existsById(id: Long): Boolean {
        return flightRepository.existsById(id)

    }

    fun deleteById(id: Long) {
        checkExist(id)
        val flight = flightRepository.getReferenceById(id)
        flight.deleted = true
        flightRepository.save(flight)
    }

    private fun checkExist(id: Long) {
        if (!flightRepository.existsById(id)) {
            throw ResourceNotFoundException("Flight ith id $id does not exist")
        }
    }

    fun update(id: Long, flight: Flight): Flight {
        checkExist(id)
        if (flight.id != id) {
            throw AnotherException("Hotel with id $id does not update")
        }
        return flightRepository.save(flight)
    }
}
