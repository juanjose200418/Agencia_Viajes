package com.example.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(indexes = [Index(name = "idx_admin_audit_actor", columnList = "actor_id"), Index(name = "idx_admin_audit_target", columnList = "target_user_id")])
data class AdminAuditLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "actor_id", nullable = false)
    var actorId: Long? = null,

    @Column(name = "actor_email", nullable = false)
    var actorEmail: String? = null,

    @Column(name = "target_user_id", nullable = false)
    var targetUserId: Long? = null,

    @Column(name = "target_email", nullable = false)
    var targetEmail: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var action: AdminAuditAction? = null,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(length = 1000)
    var details: String? = null,
)

enum class AdminAuditAction {
    ROLE_CHANGED,
    ADMIN_DELETED,
    SUPERADMIN_DELETED,
    PASSWORD_RESET_REQUESTED,
    USER_BLOCKED,
    USER_UNBLOCKED,
    USER_UPDATED
}
