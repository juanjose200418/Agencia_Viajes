package com.example.service

import BaseService
import com.example.model.File
import com.example.model.Image
import com.example.repository.ImageRepository
import org.springframework.stereotype.Service

@Service
class ImageService(
    private val _imageRepository: ImageRepository,
    private val _fileService: FileService,
) : BaseService<Image, Long>(_imageRepository) {

    override fun beforeCreate(entity: Image): Image {
        val file = _fileService.create(
            File(file = entity.imageFile, type = File.Type.IMAGE)
        )

        entity.fileId = file.id
        entity.imageFile = null

        return entity
    }

    override fun beforeUpdate(id: Long, entity: Image): Image {
        super.beforeUpdate(id, entity)

        entity.imageFile?.let {
            val file = _fileService.update(entity.fileId!!, File(file = it))
            entity.fileId = file.id
        }
        return entity
    }

    override fun beforeDelete(id: Long) {
        val image = findById(id)

        _fileService.deleteById(image.fileId!!)
    }
}
