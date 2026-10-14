package com.example.repository

import com.example.model.AdminAuditLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminAuditLogRepository : JpaRepository<AdminAuditLog, Long>
