package com.example.service

import com.example.shared.exception.ConflictException
import com.example.shared.exception.ResourceNotFoundException
import com.example.model.*
import com.example.repository.AdminAuditLogRepository
import com.example.repository.UserRepository
import com.example.security.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var auditRepository: AdminAuditLogRepository

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    fun getAll(): List<User> {
        return userRepository.findAllByDeleted(false)
    }

    fun getPage(
        page: Int,
        size: Int,
        search: String? = null,
        role: Role? = null,
        createdFrom: String? = null,
        createdTo: String? = null,
        sortBy: String? = null,
    ): Page<User> {
        return userRepository.findAll(
            userSpec(search, role, createdFrom, createdTo),
            PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 100), userSort(sortBy))
        )
    }

    fun getById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("User with id $id does not exist")
        }
    }

    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }

    @Transactional
    fun deleteById(id: Long, request: UserDeleteRequest) {
        val actor = currentActor()
        val target = getById(id)
        ensureCanManage(actor, target)
        if (actor.id == target.id) throw AccessDeniedException("You cannot delete your own user")
        if (target.role == Role.SUPER_ADMIN && activeSuperAdmins() <= 1) {
            throw ConflictException("Cannot delete the last SUPER_ADMIN")
        }
        if (target.role == Role.ADMIN || target.role == Role.SUPER_ADMIN) {
            requireReauthentication(actor, request.currentPassword)
            if (request.confirmationEmail?.trim() != target.email) {
                throw IllegalArgumentException("Confirmation email does not match")
            }
        }
        target.deleted = true
        userRepository.save(target)
        audit(actor, target, if (target.role == Role.SUPER_ADMIN) AdminAuditAction.SUPERADMIN_DELETED else AdminAuditAction.ADMIN_DELETED, "User deleted")
    }

    private fun checkExist(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("User with id $id does not exist")
        }
    }

    @Transactional
    fun update(id: Long, request: UserUpdateRequest): User {
        val actor = currentActor()
        val existingUser = getById(id)
        ensureCanManage(actor, existingUser)
        val email = required(request.email, "Email")
        val firstname = required(request.firstname, "Username")
        validateUniqueFirstname(firstname, existingUser.id)
        validateUniqueEmail(email, existingUser.id)

        if (email != existingUser.email && (existingUser.role == Role.ADMIN || existingUser.role == Role.SUPER_ADMIN)) {
            requireReauthentication(actor, request.currentPassword)
        }

        existingUser.firstname = firstname
        existingUser.lastname = request.lastname?.trim().orEmpty()
        existingUser.email = email
        existingUser.phone = request.phone?.trim().orEmpty()
        existingUser.address = request.address?.trim().orEmpty()
        val saved = userRepository.save(existingUser)
        audit(actor, existingUser, AdminAuditAction.USER_UPDATED, "User profile updated")
        return saved
    }

    @Transactional
    fun changeRole(id: Long, request: UserRoleUpdateRequest): User {
        val actor = currentActor()
        val target = getById(id)
        val newRole = request.role ?: throw IllegalArgumentException("Role is required")
        ensureCanManage(actor, target)
        if (actor.role != Role.SUPER_ADMIN) throw AccessDeniedException("Only SUPER_ADMIN can change roles")
        requireReauthentication(actor, request.currentPassword)
        if (target.role == Role.SUPER_ADMIN && newRole != Role.SUPER_ADMIN) {
            if (actor.id == target.id) throw AccessDeniedException("You cannot degrade yourself")
            if (activeSuperAdmins() <= 1) throw ConflictException("Cannot degrade the last SUPER_ADMIN")
        }
        target.role = newRole
        val saved = userRepository.save(target)
        audit(actor, target, AdminAuditAction.ROLE_CHANGED, "Role changed to $newRole")
        return saved
    }

    @Transactional
    fun requestPasswordReset(id: Long, request: PasswordResetRequest): PasswordResetResponse {
        val actor = currentActor()
        val target = getById(id)
        ensureCanManage(actor, target)
        requireReauthentication(actor, request.currentPassword)
        target.mustChangePassword = true
        userRepository.save(target)
        audit(actor, target, AdminAuditAction.PASSWORD_RESET_REQUESTED, "Password reset requested")
        return PasswordResetResponse(mustChangePassword = true)
    }

    @Transactional
    fun setBlocked(id: Long, request: UserBlockRequest): User {
        val actor = currentActor()
        val target = getById(id)
        ensureCanManage(actor, target)
        if (actor.id == target.id) throw AccessDeniedException("You cannot block your own user")
        if (target.role == Role.ADMIN || target.role == Role.SUPER_ADMIN) requireReauthentication(actor, request.currentPassword)
        target.blocked = request.blocked ?: throw IllegalArgumentException("blocked is required")
        val saved = userRepository.save(target)
        audit(actor, target, if (target.blocked == true) AdminAuditAction.USER_BLOCKED else AdminAuditAction.USER_UNBLOCKED, "Blocked=${target.blocked}")
        return saved
    }

    fun create(request: User): User {
        val email = required(request.email, "Email")
        val firstname = required(request.firstname, "Username")
        val currentRole = currentRole()
        val password = if (currentRole == null) request.pass ?: throw IllegalArgumentException("Password is required") else UUID.randomUUID().toString()
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        validateUniqueFirstname(firstname, null)
        val role = request.role ?: Role.USER
        if (currentRole == Role.ADMIN && role == Role.SUPER_ADMIN) {
            throw AccessDeniedException("ADMIN cannot create SUPER_ADMIN users")
        }
        if (currentRole == null && role != Role.USER) {
            throw AccessDeniedException("Only authenticated admins can create privileged users")
        }

        val user = User(
            email = email,
            pass = passwordEncoder.encode(password),
            firstname = firstname,
            lastname = request.lastname?.trim().orEmpty(),
            role = role,
            phone = request.phone?.trim().orEmpty(),
            address = request.address?.trim().orEmpty(),
            deleted = false,
            mustChangePassword = currentRole != null,
            blocked = false,
        )

        return userRepository.save(user)
    }

    fun findByEmail(email: String?): User {
        email ?: throw IllegalArgumentException("Email cannot be null")

        return userRepository.findByEmail(email).orElseThrow {
            IllegalArgumentException("User with email $email not found")
        }
    }

    private fun currentRole(): Role? {
        val authentication = SecurityContextHolder.getContext().authentication ?: return null
        return authentication.authorities.mapNotNull { authority ->
            runCatching { Role.valueOf(authority.authority) }.getOrNull()
        }.firstOrNull()
    }

    private fun currentUserId(): Long? {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return (principal as? User)?.id
    }

    private fun currentActor(): User {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return principal as? User ?: throw AccessDeniedException("Authenticated user is required")
    }

    private fun ensureCanManage(actor: User, target: User) {
        when (actor.role) {
            Role.SUPER_ADMIN -> return
            Role.ADMIN -> if (target.role == Role.USER) return else throw AccessDeniedException("ADMIN can only manage USER accounts")
            else -> throw AccessDeniedException("Admin privileges are required")
        }
    }

    private fun requireReauthentication(actor: User, currentPassword: String?) {
        if (currentPassword.isNullOrBlank() || !passwordEncoder.matches(currentPassword, actor.pass)) {
            throw AccessDeniedException("Current password is required for this action")
        }
    }

    private fun activeSuperAdmins(): Long = userRepository.countByRoleAndDeleted(Role.SUPER_ADMIN, false)

    private fun audit(actor: User, target: User, action: AdminAuditAction, details: String) {
        auditRepository.save(
            AdminAuditLog(
                actorId = actor.id,
                actorEmail = actor.email,
                targetUserId = target.id,
                targetEmail = target.email,
                action = action,
                details = details,
            )
        )
    }

    private fun required(value: String?, field: String): String {
        val trimmed = value?.trim()
        if (trimmed.isNullOrEmpty()) {
            throw IllegalArgumentException("$field is required")
        }
        return trimmed
    }

    private fun validateUniqueFirstname(firstname: String, currentId: Long?) {
        val exists = currentId?.let {
            userRepository.existsByFirstnameIgnoreCaseAndIdNotAndDeleted(firstname, it, false)
        } ?: userRepository.existsByFirstnameIgnoreCaseAndDeleted(firstname, false)
        if (exists) {
            throw IllegalArgumentException("Nombre de usuario ya está en uso")
        }
    }

    private fun validateUniqueEmail(email: String, currentId: Long?) {
        val existing = userRepository.findByEmail(email).orElse(null) ?: return
        if (existing.id != currentId) {
            throw IllegalArgumentException("Email is already in use")
        }
    }

    private fun userSort(sortBy: String?): Sort {
        return when (sortBy?.uppercase()) {
            "NAME_ASC" -> Sort.by("firstname").ascending().and(Sort.by("lastname").ascending())
            "NAME_DESC" -> Sort.by("firstname").descending().and(Sort.by("lastname").descending())
            "CREATED_DESC" -> Sort.by("createdAt").descending()
            "CREATED_ASC" -> Sort.by("createdAt").ascending()
            else -> Sort.by("email").ascending()
        }
    }

    private fun userSpec(search: String?, role: Role?, createdFrom: String?, createdTo: String?): Specification<User> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf(cb.isFalse(root.get("deleted")))
            search?.trim()?.takeIf { it.isNotBlank() }?.lowercase()?.let { term ->
                val pattern = "%$term%"
                predicates += cb.or(
                    cb.like(cb.lower(root.get("firstname")), pattern),
                    cb.like(cb.lower(root.get("lastname")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern),
                )
            }
            role?.let { predicates += cb.equal(root.get<Role>("role"), it) }
            createdFrom?.takeIf { it.isNotBlank() }?.let {
                predicates += cb.greaterThanOrEqualTo(root.get("createdAt"), LocalDate.parse(it).atStartOfDay())
            }
            createdTo?.takeIf { it.isNotBlank() }?.let {
                predicates += cb.lessThanOrEqualTo(root.get("createdAt"), LocalDate.parse(it).atTime(LocalTime.MAX))
            }
            cb.and(*predicates.toTypedArray())
        }
    }
}
