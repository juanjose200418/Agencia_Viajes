package com.example.service

import BaseService
import com.example.model.File
import com.example.repository.FileRepository
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileService(
    private val _fileRepository: FileRepository
) : BaseService<File, Long>(_fileRepository) {

    companion object {
        const val STORAGE_PATH = "/static/storage"
        const val URL_STORAGE_PATH = "/storage"
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

    //Antes de eliminar una entidad de la base de datos, elimina el archivo físico correspondiente.
    override fun beforeDelete(id: Long) {
        _deleteFile(id)
    }

    private fun _populateEntityFieldsFromMultipartFile(entity: File): File {
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

    //Elimina físicamente un archivo del sistema de almacenamiento.
    private fun _deleteFile(id: Long) {
        val file = findById(id)
        val folder = _getFolderFromType(file.type!!)

        val path = Paths.get(_getRootPath() + STORAGE_PATH + folder)
        val resolve = path.resolve(StringUtils.cleanPath(file.name!!))

        /*if (!Files.exists(path)) {
        } else if (!Files.exists(resolve)) {
        }*/

        Files.delete(resolve)
    }

    private fun _saveMultipartFile(multipartFile: MultipartFile, folder: String, fileName: String) {
        if (
            multipartFile.isEmpty
            || multipartFile.originalFilename.isNullOrBlank()
            || StringUtils.cleanPath(multipartFile.originalFilename!!).contains("..")
            || fileName.isBlank()
        ) {
            return
        }

        val path = Paths.get(_getRootPath() + STORAGE_PATH + folder)

        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }

        val resolve = path.resolve(StringUtils.cleanPath(fileName))
        Files.copy(multipartFile.inputStream, resolve, StandardCopyOption.REPLACE_EXISTING)
    }

    //El lowercase() se está usando para poner todos las letras de la cadena en minusculas
    private fun _getFolderFromType(type: File.Type): String {
        return "/${type.toString().lowercase()}"
    }

    //El replace sirve para que la ruta tenga un formato uniforme estilo Unix independientemente de cualquier sistema operativo
    private fun _getRootPath(): String {
        val path = Paths.get("").toAbsolutePath()
        return path.toString().replace("\\", "/")
    }
}
