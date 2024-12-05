package com.example.model

import com.example.shared.utils.Identifiable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.jetbrains.annotations.NotNull

@Entity
class ImageLibrary(
    @Id
    @GeneratedValue
    override var id: Long? = null,

    @field:NotNull
    @Column(nullable = false)
    var name: String? = null,

    var description: String? = null,

    @field:NotNull
    @Column(nullable = false)
    var published: Boolean? = null,

    ) : Identifiable<Long>, Auditing()
