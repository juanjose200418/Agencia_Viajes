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

        if (entity.fileId != null) {
            entity.imageFile?.let {
                val file = _fileService.update(entity.fileId!!, File(file = it))
                entity.fileId = file.id
            } ?: run {
                println("imageFile es nulo, no se actualizará el archivo.")
            }
        } else {
            println("fileId es nulo, creando un nuevo archivo.")
            entity.imageFile?.let {
                val file = _fileService.create(File(file = it, type = File.Type.IMAGE))
                entity.fileId = file.id
            } ?: run {
                throw IllegalArgumentException("No se proporcionó un archivo para actualizar.")
            }
        }
        return entity
    }

    override fun beforeDelete(id: Long) {
        val image = findById(id)
        println("Eliminando imagen con ID: ${image.id}, y archivo con ID: ${image.fileId}")

        super.beforeDelete(id)

        image.fileId?.let { fileId ->
            try {
                _fileService.deleteById(fileId)
            } catch (e: Exception) {
                println("No se pudo eliminar el archivo con ID: $fileId. Error: ${e.message}")
            }
        }
    }
}
