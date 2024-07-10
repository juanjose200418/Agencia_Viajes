package com.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Flight(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    val id: Long? = null,

    val flightNumber: String? = null,

    val departure: String? = null,

    val arrival: String? = null,

    val duration: Double? = null,

    val price: Double? = null,

    val airline: String? = null,

    var deleted: Boolean? = false,
)
