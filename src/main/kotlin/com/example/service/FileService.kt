package com.example.service

import BaseService
import com.example.model.File
import com.example.repository.FileRepository
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.logging.Logger

@Service
class FileService(
    private val _fileRepository: FileRepository
) : BaseService<File, Long>(_fileRepository) {

    companion object {
        const val STORAGE_PATH = "/resources/static/storage"
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
        entity.extension = StringUtils.getFilenameExtension(originalFileName)
        entity.baseName = StringUtils.stripFilenameExtension(originalFileName)

        val millis = System.currentTimeMillis()
        entity.name = "${entity.type!!}_D$millis.${entity.extension}"

        val folder = _getFolderFromType(entity.type!!)
        _saveMultipartFile(entity.file!!, folder, entity.name!!)

        entity.path = URL_STORAGE_PATH + folder
        entity.file = null

        return entity
    }

    private fun _deleteFile(id: Long) {
        val file = findById(id)
        val folder = _getFolderFromType(file.type!!)
        val path = Paths.get(_getRootPath() + STORAGE_PATH + folder)
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
        if (
            multipartFile.isEmpty
            || multipartFile.originalFilename.isNullOrBlank()
            || StringUtils.cleanPath(multipartFile.originalFilename!!).contains("..")
            || fileName.isBlank()
        ) {
            logger.warning("The file is not valid and cannot be saved")
            return
        }

        val path = Paths.get(_getRootPath() + STORAGE_PATH + folder)

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

    private fun _getFolderFromType(type: File.Type): String {
        return "/${type.toString().lowercase()}"
    }

    private fun _getRootPath(): String {
        val path = Paths.get("").toAbsolutePath()
        return path.toString().replace("\\", "/") + "/src/main"
    }
}
