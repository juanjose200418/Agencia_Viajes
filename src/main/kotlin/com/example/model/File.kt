package com.example.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.springframework.web.multipart.MultipartFile
import java.io.Serializable

@Entity
class File(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String? = null,

    @Column(nullable = false)
    var baseName: String? = null,

    @Column(nullable = false)
    var extension: String? = null,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: Type? = null,

    @Column(nullable = false)
    var path: String? = null,

    @Transient
    var file: MultipartFile? = null,
) : Serializable {
    enum class Type {
        IMAGE, SOUND
    }

    @get:Transient
    val url: String
        get() = "$path/$name"
    }
