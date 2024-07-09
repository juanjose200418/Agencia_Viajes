package com.example.service

import com.example.exception.ResourceNotFoundException
import com.example.model.Flight
import com.example.model.Hotel
import com.example.repository.HotelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HotelService {
    @Autowired
    lateinit var hotelRepository: HotelRepository

    fun getAll(): List<Hotel> {
        return hotelRepository.findAll()
    }

    fun create(hotel: Hotel): Hotel {
        return hotelRepository.save(hotel)
    }

    fun getById(id: Long): Hotel {
        return hotelRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Hotel with id $id does not exist")
        }
    }

    fun existsById(id: Long): Boolean {
        return hotelRepository.existsById(id)
    }
    fun deleteById(id: Long){
       if (!hotelRepository.existsById(id)){
           throw ResourceNotFoundException("Hotel with id $id does not exist")
       }
        hotelRepository.deleteById(id)
    }
}