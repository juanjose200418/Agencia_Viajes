package com.example.repository

import com.example.model.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : JpaRepository<Booking, Long> {
    fun findAllByDeleted(deleted: Boolean): List<Booking>
}
