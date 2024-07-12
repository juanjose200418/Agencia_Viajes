package com.example.service

import com.example.exception.AnotherException
import com.example.exception.ResourceNotFoundException
import com.example.model.Hotel
import com.example.model.User
import com.example.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getAll(): List<User> {
        return userRepository.findAllByDeleted(false)
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
    fun deleteById(id: Long){
        checkExist(id)
        val user = userRepository.getReferenceById(id)
        user.deleted = true
        userRepository.save(user)
    }

    private fun checkExist(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("User with $id does not exist")
        }
    }

    fun update(@PathVariable id : Long, @RequestBody user: User): User{
        checkExist(id)
        if (user.id != id){
         throw AnotherException ("User with id $id does not update")
        }
        return userRepository.save(user)
    }
}
