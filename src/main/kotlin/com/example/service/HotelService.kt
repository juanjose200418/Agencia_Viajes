package com.example.service

import com.example.shared.exception.AnotherException
import com.example.shared.exception.ResourceNotFoundException
import com.example.model.Hotel
import com.example.repository.DestinationRepository
import com.example.repository.HotelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class HotelService(
    private val autoImageService: AutoImageService
) {
    @Autowired
    lateinit var hotelRepository: HotelRepository

    @Autowired
    lateinit var destinationRepository: DestinationRepository

    fun getAll(): List<Hotel> {
        return hotelRepository.findAllByDeleted(false).map { withImageFallback(it) }
    }

    fun getPage(page: Int, size: Int): Page<Hotel> {
        return hotelRepository.findAllByDeleted(
            false,
            PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 100), Sort.by("name").ascending())
        ).map { withImageFallback(it) }
    }

    fun create(hotel: Hotel): Hotel {
        val destination = validateDestination(hotel.destinationId)
        hotel.imageUrl = hotel.imageUrl?.takeIf { it.isNotBlank() }
            ?: autoImageService.hotelImageUrl(hotel.name, destination.name)
        hotel.deleted = false
        return hotelRepository.save(hotel)
    }

    fun getById(id: Long): Hotel {
        return withImageFallback(hotelRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Hotel with id $id does not exist")
        })
    }

    fun existsById(id: Long): Boolean {
        return hotelRepository.existsById(id)
    }

    fun deleteById(id: Long) {
        checkExists(id)
        val hotel = hotelRepository.getReferenceById(id)
        hotel.deleted = true
        hotelRepository.save(hotel)
    }

    private fun checkExists(id: Long) {
        if (!hotelRepository.existsById(id)) {
            throw ResourceNotFoundException("Hotel with id $id does not exist")
        }
    }

    fun update(id: Long, hotel: Hotel): Hotel {
        checkExists(id)
        if (hotel.id != id) {
            throw AnotherException("Hotel with id $id does not update")
        }
        val destination = validateDestination(hotel.destinationId)
        val existing = getById(id)
        existing.name = hotel.name ?: existing.name
        existing.email = hotel.email ?: existing.email
        existing.address = hotel.address ?: existing.address
        existing.phone = hotel.phone ?: existing.phone
        existing.location = hotel.location ?: existing.location
        existing.imageUrl = hotel.imageUrl?.takeIf { it.isNotBlank() }
            ?: existing.imageUrl?.takeIf { it.isNotBlank() }
            ?: autoImageService.hotelImageUrl(existing.name, destination.name)
        existing.externalUrl = hotel.externalUrl ?: existing.externalUrl
        existing.pricePerNight = hotel.pricePerNight ?: existing.pricePerNight
        existing.rating = hotel.rating ?: existing.rating
        existing.description = hotel.description ?: existing.description
        existing.cancellationPolicy = hotel.cancellationPolicy ?: existing.cancellationPolicy
        existing.freeWifi = hotel.freeWifi ?: existing.freeWifi
        existing.breakfastIncluded = hotel.breakfastIncluded ?: existing.breakfastIncluded
        existing.pool = hotel.pool ?: existing.pool
        existing.gym = hotel.gym ?: existing.gym
        existing.spa = hotel.spa ?: existing.spa
        existing.restaurant = hotel.restaurant ?: existing.restaurant
        existing.roomService = hotel.roomService ?: existing.roomService
        existing.laundryService = hotel.laundryService ?: existing.laundryService
        existing.airportShuttle = hotel.airportShuttle ?: existing.airportShuttle
        existing.parking = hotel.parking ?: existing.parking
        existing.specialPackages = hotel.specialPackages ?: existing.specialPackages
        existing.recreationalActivities = hotel.recreationalActivities ?: existing.recreationalActivities
        existing.eventsConferences = hotel.eventsConferences ?: existing.eventsConferences
        existing.guestReviews = hotel.guestReviews ?: existing.guestReviews
        existing.socialMedia = hotel.socialMedia ?: existing.socialMedia
        existing.languages = hotel.languages ?: existing.languages
        existing.accessibility = hotel.accessibility ?: existing.accessibility
        existing.deleted = hotel.deleted ?: existing.deleted
        existing.destinationId = hotel.destinationId ?: existing.destinationId
        return hotelRepository.save(existing)
    }
    fun getAllByDestinationId(destinationId: Long): List<Hotel> {
        return hotelRepository.findByDestinationIdAndDeleted(destinationId, false).map { withImageFallback(it) }
    }

    private fun validateDestination(destinationId: Long?): com.example.model.Destination {
        destinationId ?: throw IllegalArgumentException("Destination is required")
        val destination = destinationRepository.findById(destinationId).orElseThrow {
            ResourceNotFoundException("Destination with id $destinationId does not exist")
        }
        if (destination.deleted == true) {
            throw ResourceNotFoundException("Destination with id $destinationId does not exist")
        }
        return destination
    }

    private fun withImageFallback(hotel: Hotel): Hotel {
        if (hotel.imageUrl.isNullOrBlank()) {
            val destinationName = hotel.destinationId?.let { id ->
                destinationRepository.findById(id).orElse(null)?.name
            }
            hotel.imageUrl = autoImageService.hotelImageUrl(hotel.name, destinationName)
        }
        return hotel
    }
}
