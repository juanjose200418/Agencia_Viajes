package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "idx_hotel_deleted", columnList = "deleted"),
        Index(name = "idx_hotel_destination_id", columnList = "destination_id"),
        Index(name = "idx_hotel_destination_deleted", columnList = "destination_id, deleted")
    ]
)
data class Hotel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String? = null,

    var email: String? = null,

    var address: String? = null,

    var phone: String? = null,

    var location: String? = null,

    var imageUrl: String? = null,

    var externalUrl: String? = null,

    var pricePerNight: Double? = null,

    var rating: Double? = null,

    var description: String? = null,

    var cancellationPolicy: String? = null,

    var freeWifi: Boolean? = null,

    var breakfastIncluded: Boolean? = null,

    var pool: Boolean? = null,

    var gym: Boolean? = null,

    var spa: Boolean? = null,

    var restaurant: Boolean? = null,

    var roomService: Boolean? = null,

    var laundryService: Boolean? = null,

    var airportShuttle: Boolean? = null,

    var parking: Boolean? = null,

    var specialPackages: String? = null,

    var recreationalActivities: String? = null,

    var eventsConferences: String? = null,

    var guestReviews: String? = null,

    var socialMedia: String? = null,

    var languages: String? = null,

    var accessibility: String? = null,

    var deleted: Boolean? = null,

    @Column(name = "destination_id")
    var destinationId: Long? = null,
): Auditing() {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "destination_id", insertable = false, nullable = false, updatable = false)
    var destination: Destination? = null

    @OneToOne
    @JoinColumn(name = "image_id")
    var hotelImage: Image? = null
}
