package com.example.crud

import com.example.model.Destination
import com.example.repository.DestinationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DestinationController {

    @Autowired
    lateinit var destinationRepository: DestinationRepository

    @GetMapping("/destinations")
    fun getAll(): List<Destination> {
        return destinationRepository.findAll()
    }

    @PostMapping("/destinations")
    fun create(@RequestBody destination: Destination): ResponseEntity<Destination> {
        // Asignar el destino a cada hotel dentro del destino
        destination.hotels.forEach { it.destination = destination }

        // Guardar el destino y sus hoteles asociados
        val savedDestination = destinationRepository.save(destination)

        // Retornar el destino guardado en la respuesta
        return ResponseEntity.ok(savedDestination)
    }
}

