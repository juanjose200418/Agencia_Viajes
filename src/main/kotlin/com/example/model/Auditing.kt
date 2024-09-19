package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditing(

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    var createdAt: LocalDateTime? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Long? = null,
): Serializable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    var createdByUser: User? = null
}

