package com.example.controller

import com.example.jwt.service.AuthService
import com.example.model.Credential
import com.example.model.JwtToken
import com.example.model.Role
import com.example.model.User
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var  authService: AuthService

    @PostMapping("/login")
    fun login(@RequestBody request: Credential): JwtToken {
        return authService.login(request)
    }

    @PostMapping("/register")
    fun create(@RequestBody request: User): User {
        request.role = Role.USER
        return userService.create(request)
    }
}
