package com.example.security

import com.example.model.UserIdentity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SessionManager {

    fun getUserIdentity(): UserIdentity {
        return _getAuthentication()?.principal as UserIdentity
    }

    fun isAuthenticated(): Boolean {
        val authentication = _getAuthentication()
        return authentication != null && authentication.principal is UserIdentity
    }

    fun requestingUserMatchesWithSessionId(id: Long): Boolean {
        return id == getUserIdentity().id
    }

    private fun _getAuthentication(): org.springframework.security.core.Authentication? {
        return SecurityContextHolder.getContext().authentication
    }
}
