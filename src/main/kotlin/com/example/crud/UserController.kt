package com.example.crud

import com.example.model.User
import com.example.repository.UserRepository
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController() {
    @Autowired
    lateinit var userService: UserService

    @GetMapping("/users")
    fun getAll(): List<User> {
        return userService.getAll()
    }

    @PostMapping("/users")
    fun create(@RequestBody user: User): User {
        return userService.save(user)

    }
}
