package com.example.repository

import com.example.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findAllByDeleted(deleted: Boolean): List<User>

    fun findByEmail(email: String): Optional<User>

    fun existsByEmail(email: String): Boolean
}
