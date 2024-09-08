package com.example.repository

import com.example.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findAllByDeleted(deleted: Boolean): List<User>

    fun findByUsername(username: String): Optional<User>

    fun existsByUsername(username: String): Boolean


}
