package com.example.model

import jakarta.persistence.*

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(
    indexes = [
        Index(name = "idx_destination_deleted", columnList = "deleted"),
        Index(name = "idx_destination_slug", columnList = "slug"),
        Index(name = "idx_destination_country", columnList = "country")
    ]
)
@JsonInclude(JsonInclude.Include.NON_NULL) // No serializa nulls
data class Destination(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    @JsonProperty("name")
    var name: String? = null,

    @Column(unique = true)
    @JsonProperty("slug")
    var slug: String? = null,

    @JsonProperty("country")
    var country: String? = null,

    @JsonProperty("description")
    var description: String? = null,

    @JsonProperty("location")
    var location: String? = null,

    @JsonProperty("imageUrl")
    var imageUrl: String? = null,

    @ElementCollection
    @CollectionTable(name = "destination_points_of_interest", joinColumns = [JoinColumn(name = "destination_id")])
    @Column(name = "point", nullable = false)
    @JsonProperty("pointsOfInterest")
    var pointsOfInterest: MutableList<String> = mutableListOf(),

    @JsonIgnore
    var popularAttractions: String? = null,

    @JsonProperty("averageCost")
    var averageCost: Double? = null,

    @JsonIgnore
    var deleted: Boolean? = false,

    @JsonIgnore
    @OneToMany(mappedBy = "destination")
    val hotels: MutableList<Hotel> = mutableListOf()
): Auditing()
