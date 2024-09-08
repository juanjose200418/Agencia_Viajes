package com.example.config

import com.example.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

/*
@Configuration
@EnableWebSecurity
class SecurityConfig {
    fun SecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain{
        httpSecurity.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST)
    }
}
*/