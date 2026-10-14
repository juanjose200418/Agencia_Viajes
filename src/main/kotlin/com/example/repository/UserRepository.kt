package com.example.repository

import com.example.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    fun findAllByDeleted(deleted: Boolean): List<User>

    fun findAllByDeleted(deleted: Boolean, pageable: Pageable): Page<User>

    fun findByEmail(email: String): Optional<User>

    fun existsByEmail(email: String): Boolean

    fun existsByFirstnameIgnoreCaseAndDeleted(firstname: String, deleted: Boolean): Boolean

    fun existsByFirstnameIgnoreCaseAndIdNotAndDeleted(firstname: String, id: Long, deleted: Boolean): Boolean

    fun countByRoleAndDeleted(role: com.example.model.Role, deleted: Boolean): Long
}
