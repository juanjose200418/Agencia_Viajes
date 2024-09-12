package com.example.service

import com.example.exception.AnotherException
import com.example.exception.ResourceNotFoundException
import com.example.jwt.service.AuthService
import com.example.model.*
import com.example.repository.UserRepository
import com.example.security.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
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
        checkExist(id)
        if (user.id != id) {
            throw AnotherException("User with id $id does not update")
        }
        return userRepository.save(user)
    }

    fun create(request: User): User {
        if (userRepository.existsByEmail(request.email ?: throw IllegalArgumentException("Email is required"))) {
            throw RuntimeException("User already exists")
        }

        val user = User(
            email = request.email,
            pass = passwordEncoder.encode(request.pass ?: throw IllegalArgumentException("Password is required")),
            firstname = request.firstname,
            lastname = request.lastname,
            role = "USER"
        )

        return userRepository.save(user)
    }

    fun findByEmail(email: String?): User {
        email ?: throw BadCredentialsException("Email cannot be null")

        return userRepository.findByEmail(email).orElseThrow {
            IllegalArgumentException("User with email $email not found")
        }
    }
}
