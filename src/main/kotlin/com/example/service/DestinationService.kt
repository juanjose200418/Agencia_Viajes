package com.example.service

import com.example.model.Destination
import com.example.repository.DestinationRepository
import org.springframework.beans.factory.annotation.Autowired

class DestinationService {
    @Autowired
    lateinit var destinatinationRepository: DestinationRepository

    fun getAll(): List<Destination> {
        return destinatinationRepository.findAll()
    }
}
