package com.example.model

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

    @OneToMany(mappedBy = "destination")
    val hotels: MutableList<Hotel> = mutableListOf()
)
