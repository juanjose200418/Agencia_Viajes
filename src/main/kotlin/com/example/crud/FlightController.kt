package com.example.crud

import com.example.model.Flight
import com.example.repository.FlightRepository
import com.example.service.FlightService
import jakarta.persistence.Id
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FlightController {

    @Autowired
    lateinit var flightService: FlightService

    @GetMapping("/flights")
    fun getAll(): List<Flight> {
        return flightService.getAll()
    }

    @PostMapping("/flights")
    fun create (@RequestBody flight: Flight): Flight {
        return flightService.create(flight)
    }
    @GetMapping("/flights/{id}")
    fun getById(@PathVariable id: Long): Flight{
        return flightService.getById(id)
    }
}
