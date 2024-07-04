package com.example.model

import jakarta.persistence.*
import java.time.LocalDate
import javax.print.attribute.standard.Destination

@Entity
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @ManyToOne
    val user: User,

    @ManyToOne
    val destination: Destination,

    @ManyToOne
    val hotel: Hotel,

    @ManyToOne
    val flight: Flight,

    val bookingDate: LocalDate,
    val totalCost: Double,
)


