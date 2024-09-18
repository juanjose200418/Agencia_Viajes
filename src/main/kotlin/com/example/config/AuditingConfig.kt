package com.example.config

import com.example.security.SessionManager
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditingConfig")
class AuditingConfig(
    private val _sessionManager: SessionManager
) : AuditorAware<Long> {

    override fun getCurrentAuditor(): Optional<Long> {
        val isAuthenticated = _sessionManager.isAuthenticated()
        if (isAuthenticated) {
            val identity = _sessionManager.getUserIdentity()
            return Optional.of(identity.id)
        } else {
            return Optional.empty()
        }
    }

}
