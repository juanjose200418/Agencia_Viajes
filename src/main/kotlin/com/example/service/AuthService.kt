package com.example.jwt.service

import com.example.model.AuthResponse
import com.example.model.Credential
import com.example.model.User
import com.example.security.JwtService
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    fun login(request: Credential): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password,
            )
        )
        val user: User = userService.findByEmail(request.email)
        val token: String = jwtService.getToken(user)
        return AuthResponse(token, user)
    }
}
