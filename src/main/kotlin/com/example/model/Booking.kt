// Booking.kt
package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jdk.jfr.Category
import java.time.LocalDate

@Entity
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id")
    var userId: Long? = null,

    @Column(name = "destination_id")
    var destinationId: Long? = null,

    @Column(name = "hotel_id")
    var hotelId: Long? = null,

    @Column(name = "flight_id")
    var flightId: Long? = null,

    var deleted: Boolean? = false,
    var bookingDate: LocalDate? = null,
    var totalCost: Double? = null,
) {/*Defino la relación*/
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "destination_id", insertable = false, nullable = false, updatable = false)
    var destination: Destination? = null

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "hotel_id", insertable = false, nullable = false, updatable = false)
    var hotel: Hotel? = null

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, nullable = false, updatable = false)
    var user: User? = null

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "flight_id", insertable = false, nullable = false, updatable = false)
    var flight: Flight? = null
}
