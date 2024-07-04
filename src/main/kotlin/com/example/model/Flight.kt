package com.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Flight(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val flightNumber: String,
    val departure: String,
    val arrival: String,
    val duration: Double,
    val price: Double,
    val airline: String,
)
