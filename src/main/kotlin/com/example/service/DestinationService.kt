package com.example.service

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

    fun save(destination: Destination): Destination {
        return destinationRepository.save(destination)
    }
}
