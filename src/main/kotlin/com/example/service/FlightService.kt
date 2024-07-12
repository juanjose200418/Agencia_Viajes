package com.example.service

import com.example.exception.AnotherException
import com.example.exception.ResourceNotFoundException
import com.example.model.Flight
import com.example.repository.FlightRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

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
        chekExist(id)
       val flight = flightRepository.getReferenceById(id)
        flight.deleted = true
        flightRepository.save(flight)
    }

    private fun chekExist(id: Long) {
        if (!flightRepository.existsById(id)) {
            throw ResourceNotFoundException("Flight ith id $id does not exist")
        }
    }

    fun update(@PathVariable id: Long, @RequestBody flight: Flight):Flight{
        chekExist(id)
        if (flight.id != id) {
            throw AnotherException("Hotel with id $id does not update")
        }
        return flightRepository.save(flight)
    }
}
