package com.example.service

import com.example.model.Flight
import com.example.repository.FlightRepository
import org.springframework.beans.factory.annotation.Autowired

class FlightService {
    @Autowired
    lateinit var flightRepository: FlightRepository

    fun getAll():List<Flight>{
        return flightRepository.findAll()
    }
}