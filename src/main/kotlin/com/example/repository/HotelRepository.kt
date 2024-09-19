package com.example.repository

import com.example.model.Hotel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HotelRepository : JpaRepository<Hotel, Long> {
    fun findAllByDeleted(delete: Boolean): List<Hotel>

    fun findByDestinationId(destinationId: Long): List<Hotel>
}

