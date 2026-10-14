package com.example.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@JsonIgnoreProperties(
    "password",
    "username",
    "authorities",
    "enabled",
    "accountNonExpired",
    "accountNonLocked",
    "credentialsNonExpired",
)
@Entity
@Table(
    name = "_user",
    indexes = [
        Index(name = "idx_user_deleted", columnList = "deleted"),
        Index(name = "idx_user_role", columnList = "role")
    ]
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var firstname: String? = null,

    var lastname: String? = null,

    @Column(nullable = false, unique = true)
    var email: String? = null,

    @Column(nullable = false)
    /*Esto sirve para q la contraseña no se devuelva en el Json*/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var pass: String? = null,

    @Column(nullable = false)
    var phone: String? = null,

    @Column(nullable = false)
    var address: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role? = null,

    var deleted: Boolean? = false,

    var blocked: Boolean? = false,

    var mustChangePassword: Boolean? = false,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    var userFile : Image? = null,
) : UserDetails, Auditing() {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return role?.let { mutableListOf(SimpleGrantedAuthority(it.name)) } ?: mutableListOf()
    }

    override fun getPassword(): String {
        return pass?: throw IllegalArgumentException("Password is null")
    }

    override fun getUsername(): String {
        return email?: throw IllegalArgumentException("email is null")
    }

    override fun isAccountNonLocked(): Boolean {
        return blocked != true
    }

    override fun isEnabled(): Boolean {
        return deleted != true
    }
}
