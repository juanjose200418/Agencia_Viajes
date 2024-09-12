package com.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Flight(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    var id: Long? = null,

    var flightNumber: String? = null,

    var departure: String? = null,

    var arrival: String? = null,

    var duration: Double? = null,

    var price: Double? = null,

    var airline: String? = null,

    var deleted: Boolean? = false,
)
