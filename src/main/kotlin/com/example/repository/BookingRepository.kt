package com.example.repository

import com.example.model.Booking
import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository: JpaRepository<Booking, Long> {
}