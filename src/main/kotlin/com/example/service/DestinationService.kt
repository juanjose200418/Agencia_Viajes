package com.example.service

import com.example.exception.AnotherException
import com.example.exception.ResourceNotFoundException
import com.example.model.Destination
import com.example.repository.DestinationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.xml.validation.ValidatorHandler

@Service
class DestinationService {
    @Autowired
    lateinit var destinationRepository: DestinationRepository

    fun getAll(): List<Destination> {
        return destinationRepository.findAllByDeleted(false)
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

    fun deleteById(id: Long) {
        checkExist(id)
        val destination = destinationRepository.getReferenceById(id)
        destination.deleted = true
        destinationRepository.save(destination)
    }

    private fun checkExist(id: Long) {
        if (!destinationRepository.existsById(id)) {
            throw ResourceNotFoundException("Destination with id $id does not exist")
        }
    }

    fun update(id: Long, destination: Destination): Destination {
        checkExist(id)
        if (destination.id != id) {
            throw AnotherException("Destination with id $id does not exist")
        }
        return destinationRepository.save(destination)
    }
}

