package com.example.config

import com.example.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.function.Supplier

@Configuration
class SecurityConfig {
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder:PasswordEncoder

    // Defino el bean para el AuthenticationManager, que se encarga de gestionar la autenticación.
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    // Defino el AuthenticationProvider que utiliza un DaoAuthenticationProvider para autenticar los usuarios.
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailService()) // Establezco el servicio de detalles de usuario.
        authenticationProvider.setPasswordEncoder(passwordEncoder) // Establezco el codificador de contraseñas.
        return authenticationProvider
    }

    // Defino el UserDetailsService que carga los detalles del usuario desde el UserRepository por el nombre de usuario.
    @Bean
    fun userDetailService(): UserDetailsService {
        return UserDetailsService { username: String? ->
            userRepository!!.findByEmail(username!!)
                .orElseThrow(Supplier { UsernameNotFoundException("User not found") }) // Lanza una excepción si no encuentro el usuario.
        }
    }
}
