package com.example.crud

import com.example.model.Flight
import com.example.repository.FlightRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FlightController {
    @Autowired
    lateinit var flightRepository: FlightRepository

    @GetMapping("/flights")
    fun getAll():List<Flight>{
        return flightRepository.findAll()
    }
    @PostMapping("/flights")
    fun create(@RequestBody flight: Flight):ResponseEntity<Flight>{
        val savedFlight = flightRepository.save(flight)
        return ResponseEntity.ok(savedFlight)
    }
}