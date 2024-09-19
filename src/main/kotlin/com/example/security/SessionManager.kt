package com.example.security

import com.example.model.User
import com.example.model.UserIdentity
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SessionManager {

    @Autowired
    lateinit var userService: UserService

    fun getUserIdentity(): User {
        return _getAuthentication()?.principal as User
    }

    fun isAuthenticated(): Boolean {
        val authentication = _getAuthentication()
        return authentication != null && authentication.principal is User
    }

    fun requestingUserMatchesWithSessionId(id: Long): Boolean {
        return id == getUserIdentity().id
    }

    private fun _getAuthentication(): org.springframework.security.core.Authentication? {
        return SecurityContextHolder.getContext().authentication
    }
}
