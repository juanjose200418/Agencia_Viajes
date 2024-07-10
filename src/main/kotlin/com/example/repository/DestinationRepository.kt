package com.example.repository

import com.example.model.Destination
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DestinationRepository : JpaRepository<Destination, Long> {
    fun findAllByDeleted(deleted: Boolean): List<Destination>
}
