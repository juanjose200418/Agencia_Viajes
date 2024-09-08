package com.example.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
/*

@RestController
@RequestMapping("/auth")
class AuthController {
    private val authService: AuthService? = null

    @PostMapping(value = ["login"])
    fun login(@RequestBody request: LoginRequest?): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }

    @PostMapping(value = ["register"])
    fun register(@RequestBody request: RegisterRequest?): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.register(request))
    }
}*/