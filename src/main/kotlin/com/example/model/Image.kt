package com.example.model

import com.example.shared.utils.Identifiable
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import org.springframework.web.multipart.MultipartFile

@Entity
class Image(
    @Id
    @GeneratedValue
    override var id: Long? = null,

    @field:NotNull
    @Column(nullable = false)
    var name: String? = null,

    var icon: String? = null,

    @field:NotNull
    @Transient
    var imageFile: MultipartFile? = null,
/*
    @field:NotNull
    @Column(name = "image_library_id", nullable = false)
    var imageLibraryId: Long? = null,*/

) : Identifiable<Long>, Auditing() {
/*
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "image_library_id", insertable = false, nullable = false, updatable = false)
    var imageLibrary: ImageLibrary? = null*/

    @Column(name = "file_id", nullable = false)
    var fileId: Long? = null

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    var file: File? = null

    @get:Transient
    val fileUrl: String?
        get() = file?.url

    @JsonIgnore
    @OneToOne(mappedBy = "hotelImage")
    var hotel: Hotel? = null
}
