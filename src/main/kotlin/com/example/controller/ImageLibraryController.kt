package com.example.controller

import com.example.model.ImageLibrary
import com.example.security.SessionManager
import com.example.service.ImageLibraryService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class ImageLibraryController(
    private val _imageLibraryService: ImageLibraryService,
    private val _sessionManager: SessionManager,
) {

    @GetMapping("/sound-libraries/{id}")
    fun getById(@PathVariable id: Long): ImageLibrary {
        return _imageLibraryService.findById(id)
    }

    @GetMapping("/sound-libraries")
    fun findAll(pageable: Pageable): Page<ImageLibrary> {
        return _imageLibraryService.findAll(pageable)
    }

    @PostMapping("/sound-libraries")
    fun create(@Valid @RequestBody soundLibrary: ImageLibrary): ImageLibrary {
        return _imageLibraryService.create(soundLibrary)
    }

    @PreAuthorize("@_sessionManager.requestingSoundLibraryMatchesWithSessionId(#id) or hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("/sound-libraries/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody soundLibrary: ImageLibrary,
    ): ImageLibrary {
        return _imageLibraryService.update(id, soundLibrary)
    }

    @PreAuthorize("@_sessionManager.requestingSoundLibraryMatchesWithSessionId(#id) or hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/sound-libraries/{id}")
    fun delete(@PathVariable id: Long) {
        _imageLibraryService.deleteById(id)
    }
}
