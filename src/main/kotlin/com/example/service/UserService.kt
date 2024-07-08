package com.example.service

import com.example.model.User
import com.example.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getAll(): List<User> {
        return userRepository.findAll()
    }
    fun save(user: User ): User{
        return userRepository.save(user)
    }
}
