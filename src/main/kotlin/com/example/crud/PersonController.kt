package com.example.crud

import com.example.model.Person
import com.example.repository.PersonRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/person")
class PersonController(private val personRepository: PersonRepository) {

    @GetMapping
    fun helloWorld(): String = "Hola, esto es un REST endpoint"

    @PostMapping
    fun createPerson(@RequestBody person: Person): ResponseEntity<Person> {
        val savedPerson = personRepository.save(person)
        return ResponseEntity.ok(savedPerson)
    }
}
