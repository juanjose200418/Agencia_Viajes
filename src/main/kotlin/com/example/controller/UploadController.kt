package com.example.controller

import com.example.model.File
import com.example.model.ImageUploadResponse
import com.example.service.FileService
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class UploadController(
    private val fileService: FileService
) {
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/uploads/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestPart("file") file: MultipartFile): ImageUploadResponse {
        val stored = fileService.create(File(file = file, type = File.Type.IMAGE))
        return ImageUploadResponse(stored.url)
    }
}
