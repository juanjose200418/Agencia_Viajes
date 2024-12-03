package com.example.model

import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile
import java.io.Serializable

@Entity
class ImageFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String? = null,

    @Column(nullable = false)
    var baseName: String? = null,

    @Column(nullable = false)
    var extension: String? = null,

    @Column(nullable = false)
    var path: String? = null,

    @Transient
    var file: MultipartFile? = null,
) : Serializable {

    @get:Transient
    val url: String
        get() = "$path/$name"
    }
