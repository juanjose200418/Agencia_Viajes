package com.example.repository

import com.example.model.Destination
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DestinationRepository : JpaRepository<Destination, Long>, JpaSpecificationExecutor<Destination> {
    fun findAllByDeleted(deleted: Boolean): List<Destination>
    fun findAllByDeleted(deleted: Boolean, pageable: Pageable): Page<Destination>
    fun findBySlugAndDeleted(slug: String, deleted: Boolean): Destination?

    @Query("select distinct d.country from Destination d where d.deleted = false and d.country is not null order by d.country")
    fun findDistinctCountries(): List<String>

    @Query("select distinct d.location from Destination d where d.deleted = false and d.location is not null order by d.location")
    fun findDistinctLocations(): List<String>
}
