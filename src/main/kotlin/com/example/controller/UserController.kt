package com.example.controller

import com.example.model.User
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    fun create(@RequestBody user: User):User{
        return userService.create(user)
    }

    @PreAuthorize ("hasRole('ADMIN')")
    @GetMapping("/users")
    fun getAll(): List<User>{
        return userService.getAll()
    }

    @GetMapping("/users/{id}")
    fun getById(@PathVariable id: Long): User {
        return userService.getById(id)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    fun deleteById(@PathVariable id: Long) {
        userService.deleteById(id)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User): User {
        return userService.update(id, user)
    }

}
