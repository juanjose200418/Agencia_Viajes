package com.example.crud

import com.example.model.User
import com.example.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController() {
    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/users")
    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    @PostMapping("/users")
    fun create(@RequestBody user: User): ResponseEntity<User> {
        val savedUser = userRepository.save(user)
        return ResponseEntity.ok(savedUser)

    }
}
