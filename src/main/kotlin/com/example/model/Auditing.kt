package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.security.Timestamp

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditing(

    @CreatedDate
    @Column(updatable = false, nullable = false)
    var createdAt: Timestamp? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false, nullable = false)
    var createdBy: Long? = null,
): Serializable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    var createdByUser: User? = null
}
