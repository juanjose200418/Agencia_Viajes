package com.example.service

import com.example.model.*
import com.example.repository.AdminAuditLogRepository
import com.example.repository.UserRepository
import com.example.shared.exception.ConflictException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.util.ReflectionTestUtils
import java.util.Optional

class UserServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var auditRepository: AdminAuditLogRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        auditRepository = mock(AdminAuditLogRepository::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        userService = UserService()
        ReflectionTestUtils.setField(userService, "userRepository", userRepository)
        ReflectionTestUtils.setField(userService, "auditRepository", auditRepository)
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder)
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `create rejects duplicated username`() {
        val request = User(
            firstname = "super",
            email = "new-user@example.com",
            pass = "Password123!",
            role = Role.USER,
        )

        `when`(userRepository.existsByEmail("new-user@example.com")).thenReturn(false)
        `when`(userRepository.existsByFirstnameIgnoreCaseAndDeleted("super", false)).thenReturn(true)

        assertThatThrownBy { userService.create(request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Nombre de usuario ya está en uso")

        verify(userRepository, never()).save(ArgumentMatchers.any(User::class.java))
    }

    @Test
    fun `create normalizes user data and encodes password`() {
        val request = User(
            firstname = "  demoUser  ",
            lastname = "  Portfolio  ",
            email = "  demo@example.com  ",
            pass = "Password123!",
            phone = "  +34 600 000 000  ",
            address = "  Madrid  ",
            role = Role.USER,
        )

        `when`(userRepository.existsByEmail("demo@example.com")).thenReturn(false)
        `when`(userRepository.existsByFirstnameIgnoreCaseAndDeleted("demoUser", false)).thenReturn(false)
        `when`(passwordEncoder.encode("Password123!")).thenReturn("encoded-password")
        `when`(userRepository.save(ArgumentMatchers.any(User::class.java))).thenAnswer { it.arguments.first() }

        userService.create(request)

        val captor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(captor.capture())
        assertThat(captor.value.firstname).isEqualTo("demoUser")
        assertThat(captor.value.lastname).isEqualTo("Portfolio")
        assertThat(captor.value.email).isEqualTo("demo@example.com")
        assertThat(captor.value.phone).isEqualTo("+34 600 000 000")
        assertThat(captor.value.address).isEqualTo("Madrid")
        assertThat(captor.value.pass).isEqualTo("encoded-password")
        assertThat(captor.value.role).isEqualTo(Role.USER)
    }

    @Test
    fun `super admin cannot delete itself`() {
        authenticateAs(User(id = 1, email = "owner@example.com", role = Role.SUPER_ADMIN))
        val protectedUser = User(id = 1, email = "owner@example.com", role = Role.SUPER_ADMIN, deleted = false)

        `when`(userRepository.findById(1)).thenReturn(Optional.of(protectedUser))

        assertThatThrownBy { userService.deleteById(1, UserDeleteRequest("owner@example.com", "Secret123!")) }
            .isInstanceOf(AccessDeniedException::class.java)
            .hasMessage("You cannot delete your own user")

        verify(userRepository, never()).save(ArgumentMatchers.any(User::class.java))
    }

    @Test
    fun `admin cannot modify super admin`() {
        authenticateAs(User(id = 1, email = "admin@example.com", role = Role.ADMIN))
        val target = User(id = 2, email = "super@example.com", firstname = "super", role = Role.SUPER_ADMIN, deleted = false)
        `when`(userRepository.findById(2)).thenReturn(Optional.of(target))

        assertThatThrownBy {
            userService.update(2, UserUpdateRequest("super", "", "super@example.com", "", ""))
        }.isInstanceOf(AccessDeniedException::class.java)
    }

    @Test
    fun `admin cannot promote anyone to super admin`() {
        authenticateAs(User(id = 1, email = "admin@example.com", role = Role.ADMIN))
        val target = User(id = 2, email = "user@example.com", firstname = "user", role = Role.USER, deleted = false)
        `when`(userRepository.findById(2)).thenReturn(Optional.of(target))

        assertThatThrownBy {
            userService.changeRole(2, UserRoleUpdateRequest(Role.SUPER_ADMIN, "Secret123!"))
        }.isInstanceOf(AccessDeniedException::class.java)
    }

    @Test
    fun `user cannot access admin service operations`() {
        authenticateAs(User(id = 1, email = "user@example.com", role = Role.USER))
        val target = User(id = 2, email = "other@example.com", firstname = "other", role = Role.USER, deleted = false)
        `when`(userRepository.findById(2)).thenReturn(Optional.of(target))

        assertThatThrownBy {
            userService.update(2, UserUpdateRequest("other", "", "other@example.com", "", ""))
        }.isInstanceOf(AccessDeniedException::class.java)
    }

    @Test
    fun `cannot delete last super admin`() {
        authenticateAs(User(id = 1, email = "owner@example.com", pass = "encoded", role = Role.SUPER_ADMIN))
        val target = User(id = 2, email = "other-super@example.com", role = Role.SUPER_ADMIN, deleted = false)
        `when`(userRepository.findById(2)).thenReturn(Optional.of(target))
        `when`(userRepository.countByRoleAndDeleted(Role.SUPER_ADMIN, false)).thenReturn(1)

        assertThatThrownBy {
            userService.deleteById(2, UserDeleteRequest("other-super@example.com", "Secret123!"))
        }.isInstanceOf(ConflictException::class.java)
    }

    @Test
    fun `wrong reauthentication password is rejected`() {
        authenticateAs(User(id = 1, email = "owner@example.com", pass = "encoded", role = Role.SUPER_ADMIN))
        val target = User(id = 2, email = "admin@example.com", role = Role.ADMIN, deleted = false)
        `when`(userRepository.findById(2)).thenReturn(Optional.of(target))
        `when`(passwordEncoder.matches("wrong", "encoded")).thenReturn(false)

        assertThatThrownBy {
            userService.deleteById(2, UserDeleteRequest("admin@example.com", "wrong"))
        }.isInstanceOf(AccessDeniedException::class.java)
    }

    @Test
    fun `super admin can update another admin with reauthentication`() {
        authenticateAs(User(id = 1, email = "owner@example.com", pass = "encoded", role = Role.SUPER_ADMIN))
        val target = User(id = 2, email = "admin@example.com", firstname = "admin", role = Role.ADMIN, deleted = false)
        `when`(userRepository.findById(2)).thenReturn(Optional.of(target))
        `when`(userRepository.findByEmail("new-admin@example.com")).thenReturn(Optional.empty())
        `when`(userRepository.existsByFirstnameIgnoreCaseAndIdNotAndDeleted("admin", 2, false)).thenReturn(false)
        `when`(passwordEncoder.matches("Secret123!", "encoded")).thenReturn(true)
        `when`(userRepository.save(ArgumentMatchers.any(User::class.java))).thenAnswer { it.arguments.first() }
        `when`(auditRepository.save(ArgumentMatchers.any(AdminAuditLog::class.java))).thenAnswer { it.arguments.first() }

        val result = userService.update(2, UserUpdateRequest("admin", "", "new-admin@example.com", "", "", "Secret123!"))

        assertThat(result.email).isEqualTo("new-admin@example.com")
    }

    private fun authenticateAs(user: User) {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user,
            null,
            listOf(SimpleGrantedAuthority(user.role!!.name)),
        )
    }
}
