package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
data class Hotel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String? = null,

    var email: String? = null,

    var address: String? = null,

    var phone: String? = null,

    var location: String? = null,

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

    @Transient
    var hotelFile : ImageFile? = null,

    @Column(name = "destination_id")
    var destinationId: Long? = null,
): Auditing() {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "destination_id", insertable = false, nullable = false, updatable = false)
    var destination: Destination? = null
}
