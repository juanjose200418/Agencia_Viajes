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

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    @PostMapping("/users")
    fun create(@RequestBody user: User):User{
        return userService.create(user)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/users")
    fun getAll(): List<User>{
        return userService.getAll()
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN') or #id == authentication.principal.id")
    @GetMapping("/users/{id}")
    fun getById(@PathVariable id: Long): User {
        return userService.getById(id)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/users/{id}")
    fun deleteById(@PathVariable id: Long) {
        userService.deleteById(id)
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN') or #id == authentication.principal.id")
    @PutMapping("/users/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User): User {
        return userService.update(id, user)
    }
}
