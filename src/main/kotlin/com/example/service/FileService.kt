package com.example.service

import com.example.model.File
import com.example.repository.FileRepository
import com.example.shared.utils.BaseService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID
import java.util.logging.Logger

@Service
class FileService(
    private val _fileRepository: FileRepository,
    @Value("\${file.storage.path:./storage}") private val _storagePath: String,
    @Value("\${file.storage.max-image-size-bytes:5242880}") private val _maxImageSizeBytes: Long
) : BaseService<File, Long>(_fileRepository) {

    companion object {
        const val URL_STORAGE_PATH = "/storage"
        private val logger: Logger = Logger.getLogger(FileService::class.java.name)
    }

    override fun beforeCreate(entity: File): File {
        return _populateEntityFieldsFromMultipartFile(entity)
    }

    override fun beforeUpdate(id: Long, entity: File): File {
        entity.id = id

        if (entity.file != null) {
            _deleteFile(id)
            _populateEntityFieldsFromMultipartFile(entity)
        }

        return entity
    }

    override fun beforeDelete(id: Long) {
        _deleteFile(id)
    }

    private fun _populateEntityFieldsFromMultipartFile(entity: File): File {
        if (entity.file == null) {
            throw IllegalArgumentException("File is null")
        }
        val originalFileName = entity.file!!.originalFilename!!
        entity.extension = StringUtils.getFilenameExtension(originalFileName)?.lowercase()
        entity.baseName = StringUtils.stripFilenameExtension(StringUtils.cleanPath(originalFileName))
        validateFile(entity)

        entity.name = "${entity.type!!}_${UUID.randomUUID()}.${entity.extension}"

        val folder = _getFolderFromType(entity.type!!)
        _saveMultipartFile(entity.file!!, folder, entity.name!!)

        entity.path = URL_STORAGE_PATH + folder
        entity.file = null

        return entity
    }

    private fun _deleteFile(id: Long) {
        val file = findById(id)
        val folder = _getFolderFromType(file.type!!)
        val path = Paths.get(_storagePath, folder)
        val resolve: Path = path.resolve(StringUtils.cleanPath(file.name!!))

        try {
            if (Files.exists(resolve)) {
                Files.delete(resolve)
                logger.info("File deleted successfully: $resolve")
            } else {
                logger.warning("File does not exist and cannot be deleted: $resolve")
            }
        } catch (e: Exception) {
            logger.severe("Error trying to delete the file: ${e.message}")
        }
    }

    private fun _saveMultipartFile(multipartFile: MultipartFile, folder: String, fileName: String) {
        if (multipartFile.isEmpty || multipartFile.originalFilename.isNullOrBlank() || fileName.isBlank()) {
            throw IllegalArgumentException("The file is not valid and cannot be saved")
        }

        val path = Paths.get(_storagePath, folder)

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path)
                logger.info("Directory created: $path")
            }

            val resolve = path.resolve(StringUtils.cleanPath(fileName))
            Files.copy(multipartFile.inputStream, resolve, StandardCopyOption.REPLACE_EXISTING)
            logger.info("File saved successfully at: $resolve")
        } catch (e: Exception) {
            logger.severe("Error saving the file: ${e.message}")
        }
    }

    private fun validateFile(entity: File) {
        val multipartFile = entity.file ?: throw IllegalArgumentException("File is required")
        val safeOriginalName = StringUtils.cleanPath(multipartFile.originalFilename ?: "")
        if (safeOriginalName.isBlank() || safeOriginalName.contains("..")) {
            throw IllegalArgumentException("Invalid file name")
        }
        if (entity.type == File.Type.IMAGE) {
            val allowedExtensions = setOf("jpg", "jpeg", "png", "webp")
            val allowedContentTypes = setOf("image/jpeg", "image/png", "image/webp")
            if (entity.extension !in allowedExtensions) {
                throw IllegalArgumentException("Only JPG, JPEG, PNG and WEBP images are allowed")
            }
            if (multipartFile.contentType !in allowedContentTypes) {
                throw IllegalArgumentException("Invalid image content type")
            }
            if (multipartFile.size > _maxImageSizeBytes) {
                throw IllegalArgumentException("Image exceeds max size")
            }
        }
    }

    private fun _getFolderFromType(type: File.Type): String {
        return "/${type.toString().lowercase()}"
    }
}
