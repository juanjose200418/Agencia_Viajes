package com.example.service

import com.example.shared.exception.ResourceNotFoundException
import com.example.model.User
import com.example.repository.UserRepository
import com.example.security.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    fun getAll(): List<User> {
        return userRepository.findAllByDeleted(false)
    }

    fun getById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("User with id $id does not exist")
        }
    }

    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }

    fun deleteById(id: Long) {
        checkExist(id)
        val user = userRepository.getReferenceById(id)
        user.deleted = true
        userRepository.save(user)
    }

    private fun checkExist(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("User with id $id does not exist")
        }
    }

    fun update(id: Long, user: User): User {

        val existingUser = getById(id)

        if (user.email.isNullOrEmpty()) {
            throw IllegalArgumentException("Email cannot be null or empty")
        }

        if (user.firstname.isNullOrEmpty()) {
            throw IllegalArgumentException("First name cannot be null or empty")
        }

        existingUser.firstname = user.firstname ?: existingUser.firstname
        existingUser.lastname = user.lastname ?: existingUser.lastname
        existingUser.email = user.email ?: existingUser.email
        existingUser.phone = user.phone ?: existingUser.phone
        existingUser.address = user.address ?: existingUser.address

        if (user.pass != null) {
            existingUser.pass = passwordEncoder.encode(user.pass)
        }

        // Verifico si el usuario es administrador antes de permitir modificar rol o estado eliminado
        val authentication = SecurityContextHolder.getContext().authentication
        val isAdmin = authentication.authorities.any { it.authority == "ADMIN" }

        if (isAdmin) {
            existingUser.role = user.role ?: existingUser.role
            existingUser.deleted = user.deleted ?: existingUser.deleted
        }

        return userRepository.save(existingUser)
    }

    fun create(request: User): User {
        if (userRepository.existsByEmail(request.email ?: throw IllegalArgumentException("Email is required"))) {
            throw RuntimeException("User already exists")
        }

        val user = User(
            email = request.email,
            pass = passwordEncoder.encode(request.pass),
            firstname = request.firstname,
            lastname = request.lastname,
            role = request.role,
            phone = request.phone,
            address = request.address,
            deleted = false
        )

        return userRepository.save(user)
    }

    fun findByEmail(email: String?): User {
        email ?: throw IllegalArgumentException("Email cannot be null")

        return userRepository.findByEmail(email).orElseThrow {
            IllegalArgumentException("User with email $email not found")
        }
    }
}
