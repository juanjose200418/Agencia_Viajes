// Destination.kt
package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
data class Destination(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val name: String? = null,

    val description: String? = null,

    val location: String? = null,

    val popularAttractions: String? = null,

    val averageCost: Double? = null,

    var deletedDestination: Boolean = false,

    @JsonIgnore
    @OneToMany(mappedBy = "destination")
    val hotels: MutableList<Hotel> = mutableListOf()
)
