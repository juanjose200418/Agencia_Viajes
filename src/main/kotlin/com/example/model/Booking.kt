// Booking.kt
package com.example.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne
    @JoinColumn(name = "destination_id")
    val destination: Destination? = null,

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    val hotel: Hotel? = null,

    @ManyToOne
    @JoinColumn(name = "flight_id")
    val flight: Flight? = null,

    val bookingDate: LocalDate? = null,
    val totalCost: Double? = null,
)
