package com.example.model

data class UserUpdateRequest(
    val firstname: String?,
    val lastname: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val currentPassword: String? = null,
)

data class UserRoleUpdateRequest(
    val role: Role?,
    val currentPassword: String?,
)

data class UserDeleteRequest(
    val confirmationEmail: String?,
    val currentPassword: String?,
)

data class PasswordResetRequest(
    val currentPassword: String?,
)

data class UserBlockRequest(
    val blocked: Boolean?,
    val currentPassword: String?,
)

data class PasswordResetResponse(
    val mustChangePassword: Boolean,
)
