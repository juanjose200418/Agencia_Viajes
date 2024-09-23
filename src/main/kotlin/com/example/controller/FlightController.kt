package com.example.controller

import com.example.model.Flight
import com.example.service.FlightService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class FlightController {

    @Autowired
    lateinit var flightService: FlightService

    @GetMapping("/flights")
    fun getAll(): List<Flight> {
        return flightService.getAll()
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/flights")
    fun create(@RequestBody flight: Flight): Flight {
        return flightService.create(flight)
    }

    @GetMapping("/flights/{id}")
    fun getById(@PathVariable id: Long): Flight {
        return flightService.getById(id)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/flights/{id}")
    fun deleteById(@PathVariable id: Long) {
        return flightService.deleteById(id)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("flights/{id}")
    fun update(@PathVariable id: Long, @RequestBody flight: Flight): Flight {
        return flightService.update(id, flight)
    }
}
