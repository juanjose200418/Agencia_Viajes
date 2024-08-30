package com.example.model

import jakarta.persistence.*

data class UserDto(

    val email: String? = null,

    val password: String? = null,
)