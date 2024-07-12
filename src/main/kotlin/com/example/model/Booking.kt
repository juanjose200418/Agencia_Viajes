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
    val id: Long? = null,

    @Column(name = "user_id")
    val userId: Long? = null,

    @Column(name = "destination_id")
    val destinationId: Long? = null,

    @Column(name = "hotel_id")
    val hotelId: Long? = null,

    @Column(name = "flight_id")
    val flightId: Long? = null,

    var deleted: Boolean? = false,
    val bookingDate: LocalDate? = null,
    val totalCost: Double? = null,
) {
    //defino la relacion Muy importante
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
