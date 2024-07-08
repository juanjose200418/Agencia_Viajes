package com.example.service

import com.example.model.Flight
import com.example.model.Hotel
import com.example.repository.HotelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HotelService {
    @Autowired
    lateinit var hotelRepository: HotelRepository

    fun getAll():List<Hotel>{
        return hotelRepository.findAll()
    }
    fun save (hotel: Hotel ):Hotel{
        return hotelRepository.save(hotel)
    }
}
