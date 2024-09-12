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

class ApplicationConfig {
    @Autowired
    lateinit var userRepository: UserRepository

    // Define el bean para el AuthenticationManager, encargado de gestionar la autenticación.
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    // Define el AuthenticationProvider que utiliza un DaoAuthenticationProvider para autenticar los usuarios.
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailService()) // Establece el servicio de detalles de usuario.
        authenticationProvider.setPasswordEncoder(passwordEncoder()) // Establece el codificador de contraseñas.
        return authenticationProvider
    }

    // Define el bean del codificador de contraseñas utilizando BCrypt para encriptar las contraseñas.
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // Define el UserDetailsService que carga los detalles del usuario desde el UserRepository por el nombre de usuario.
    @Bean
    fun userDetailService(): UserDetailsService {
        return UserDetailsService { username: String? ->
            userRepository!!.findByEmail(username!!)
                .orElseThrow(Supplier { UsernameNotFoundException("User not found") }) // Lanza una excepción si el usuario no es encontrado.
        }
    }
}