package com.example.model

import jakarta.persistence.*

@Entity
data class Hotel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val ubication: String? = null,
    val description: String? = null,
    val cancellationPolicy: String? = null,
    val freeWifi: Boolean? = null,
    val breakfastIncluded: Boolean? = null,
    val pool: Boolean? = null,
    val gym: Boolean? = null,
    val spa: Boolean? = null,
    val restaurant: Boolean? = null,
    val roomService: Boolean? = null,
    val laundryService: Boolean? = null,
    val airportShuttle: Boolean? = null,
    val parking: Boolean? = null,
    val specialPackages: String? = null,
    val recreationalActivities: String? = null,
    val eventsConferences: String? = null,
    val guestReviews: String? = null,
    val socialMedia: String? = null,
    val languages: String? = null,
    val accessibility: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    var destination: Destination? = null
)
