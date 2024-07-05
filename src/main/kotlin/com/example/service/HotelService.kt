package com.example.service

import com.example.model.Hotel
import com.example.repository.HotelRepository
import org.springframework.beans.factory.annotation.Autowired

class HotelService {
    @Autowired
    lateinit var hotelRepository: HotelRepository

    fun getAll():List<Hotel>{
        return hotelRepository.findAll()
    }
}
