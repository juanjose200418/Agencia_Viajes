package com.example.service

import com.example.exception.ResourceNotFoundException
import com.example.model.Hotel
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

    fun create(user: User): User {
        return userRepository.save(user)
    }
    fun getById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("User with id $id does not exist")
        }
    }

    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }
}
