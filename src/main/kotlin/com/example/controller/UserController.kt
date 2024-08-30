package com.example.controller

import com.example.model.User
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/users")
    fun getAll(): List<User> {
        return userService.getAll()
    }

    @PostMapping("/users")
    fun create(@RequestBody user: User): User {
        return userService.create(user)
    }

    @GetMapping("/users/{id}")
    fun getById(@PathVariable id: Long): User {
        return userService.getById(id)
    }

    @DeleteMapping("/users/{id}")
    fun deleteById(@PathVariable id: Long) {
        userService.deleteById(id)
    }

    @PutMapping("/users/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User): User {
        return userService.update(id, user)
    }
}
