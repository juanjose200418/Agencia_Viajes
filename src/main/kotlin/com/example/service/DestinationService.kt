package com.example.service

import com.example.exception.ResourceNotFoundException
import com.example.model.Destination
import com.example.repository.DestinationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DestinationService {
    @Autowired
    lateinit var destinationRepository: DestinationRepository

    fun getAll(): List<Destination> {
        return destinationRepository.findAll()
    }

    fun create(destination: Destination): Destination {
        return destinationRepository.save(destination)
    }

    fun getById(id: Long): Destination {
        return destinationRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Destination with id $id does not exist")
        }
    }

    fun existsById(id: Long): Boolean {
        return destinationRepository.existsById(id)
    }
}

