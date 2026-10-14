package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "idx_flight_deleted", columnList = "deleted"),
        Index(name = "idx_flight_destination_id", columnList = "destination_id"),
        Index(name = "idx_flight_destination_deleted", columnList = "destination_id, deleted")
    ]
)
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

    var origin: String? = null,

    var stops: Int? = null,

    var deleted: Boolean? = false,

    @Column(name = "destination_id")
    var destinationId: Long? = null,
): Auditing() {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "destination_id", insertable = false, nullable = false, updatable = false)
    var destination: Destination? = null
}
