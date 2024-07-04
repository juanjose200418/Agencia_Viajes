package com.example.crud

import com.example.model.User
import com.example.repository.PersonRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/persons")
class PersonController(private val personRepository: PersonRepository) {

    @GetMapping
    fun helloWorld(): String = "Facking panza"

    @PostMapping
    fun createPerson(@RequestBody person: User): ResponseEntity<User> {
        val savedPerson = personRepository.save(person)
        return ResponseEntity.ok(savedPerson)
    }
}
