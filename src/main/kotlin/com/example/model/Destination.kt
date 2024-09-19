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
    var name: String? = null,

    var description: String? = null,

    var location: String? = null,

    var popularAttractions: String? = null,

    var averageCost: Double? = null,

    var deleted: Boolean? = false,

    @JsonIgnore
    @OneToMany(mappedBy = "destination")
    val hotels: MutableList<Hotel> = mutableListOf()
): Auditing()
