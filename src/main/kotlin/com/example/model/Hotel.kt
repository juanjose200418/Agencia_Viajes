package com.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Hotel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    val email: String,
    val address: String,
    val phone: String,
    val ubication: String,
    val description: String,
//    val cancellationPolicy: String,
//    val freeWifi: Boolean,
//    val breakfastIncluded: Boolean,
//    val pool: Boolean,
//    val gym: Boolean,
//    val spa: Boolean,
//    val restaurant: Boolean,
//    val roomService: Boolean,
//    val laundryService: Boolean,
//    val airportShuttle: Boolean,
//    val parking: Boolean,
//    val specialPackages: String,
//    val recreationalActivities: String,
//    val eventsConferences: String,
//    val guestReviews: String,
//    val socialMedia: String,
//    val languages: String,
//    val accessibility: String
)
