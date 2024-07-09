package com.example.service

import com.example.exception.ResourceNotFoundException
import com.example.model.Flight
import com.example.repository.FlightRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FlightService {
    @Autowired
    lateinit var flightRepository: FlightRepository

    fun getAll(): List<Flight> {
        return flightRepository.findAll()
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
}
