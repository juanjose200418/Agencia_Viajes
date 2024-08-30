// User.kt
package com.example.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String? = null,

    val email: String? = null,

    /*Esto sirve para q la contraseña no se devuelva en el Json*/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String? = null,

    val phone: String? = null,

    val address: String? = null,

    val role: String? = null,

    var deleted: Boolean? = false,

    )
