package com.example.controller

import com.example.model.Image
import com.example.security.SessionManager
import com.example.service.ImageService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class ImageController(
    private val _imageService: ImageService,
    private val _sessionManager: SessionManager,
) {

    @GetMapping("/images/{id}")
    fun getById(@PathVariable id: Long): Image {
        return _imageService.findById(id)
    }

    @GetMapping("/images")
    fun findAll(pageable: Pageable): Page<Image> {
        return _imageService.findAll(pageable)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/images")
    fun create(@Valid @ModelAttribute image: Image): Image {
        return _imageService.create(image)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/images/{id}")
    fun update(@PathVariable id: Long, @Valid @ModelAttribute image: Image, ): Image {
        return _imageService.update(id, image)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/images/{id}")
    fun delete(@PathVariable id: Long) {
        return _imageService.deleteById(id)
    }
}
