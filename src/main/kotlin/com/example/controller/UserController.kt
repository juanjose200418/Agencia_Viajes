package com.example.controller

import com.example.model.Role
import com.example.model.User
import com.example.model.PasswordResetRequest
import com.example.model.PasswordResetResponse
import com.example.model.UserBlockRequest
import com.example.model.UserDeleteRequest
import com.example.model.UserRoleUpdateRequest
import com.example.model.UserUpdateRequest
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/users")
    fun create(@RequestBody user: User):User{
        return userService.create(user)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/users")
    fun getAll(): List<User>{
        return userService.getAll()
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/users", params = ["page", "size"])
    fun getPage(
        @RequestParam page: Int,
        @RequestParam size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) role: Role?,
        @RequestParam(required = false) createdFrom: String?,
        @RequestParam(required = false) createdTo: String?,
        @RequestParam(required = false) sortBy: String?,
    ): Page<User> {
        return userService.getPage(page, size, search, role, createdFrom, createdTo, sortBy)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN') or #id == authentication.principal.id")
    @GetMapping("/users/{id}")
    fun getById(@PathVariable id: Long): User {
        return userService.getById(id)
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    @DeleteMapping("/users/{id}")
    fun deleteById(@PathVariable id: Long, @RequestBody request: UserDeleteRequest) {
        userService.deleteById(id, request)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN') or #id == authentication.principal.id")
    @PutMapping("/users/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UserUpdateRequest): User {
        return userService.update(id, request)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/users/{id}/role")
    fun changeRole(@PathVariable id: Long, @RequestBody request: UserRoleUpdateRequest): User {
        return userService.changeRole(id, request)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("/users/{id}/password-reset")
    fun requestPasswordReset(@PathVariable id: Long, @RequestBody request: PasswordResetRequest): PasswordResetResponse {
        return userService.requestPasswordReset(id, request)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/users/{id}/blocked")
    fun setBlocked(@PathVariable id: Long, @RequestBody request: UserBlockRequest): User {
        return userService.setBlocked(id, request)
    }
}
