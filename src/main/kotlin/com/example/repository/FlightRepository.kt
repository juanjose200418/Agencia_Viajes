package com.example.repository

import com.example.model.Flight
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FlightRepository : JpaRepository<Flight, Long> {
    fun findAllByDeleted(deleted: Boolean): List<Flight>
    fun findByDestinationId(destinationId: Long): List<Flight>
}
