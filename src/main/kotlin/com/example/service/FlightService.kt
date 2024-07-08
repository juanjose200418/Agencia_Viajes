package com.example.service

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

    fun save(flight: Flight): Flight {
        return flightRepository.save(flight)
    }
}
