// User.kt
package com.example.model

import jakarta.persistence.*

@Entity
@Table(name = "_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val role: String? = null,
)
