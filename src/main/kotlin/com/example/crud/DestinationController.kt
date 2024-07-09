package com.example.crud

import com.example.model.Destination
import com.example.service.DestinationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DestinationController {

    @Autowired
    lateinit var destinationService: DestinationService

    @GetMapping("/destinations")
    fun getAll(): List<Destination> {
        return destinationService.getAll()
    }

    @PostMapping("/destinations")
    fun create(@RequestBody destination: Destination): Destination {
        return destinationService.create(destination)
    }

    @GetMapping("/destinations/{id}")
    fun getById(@PathVariable id : Long):Destination{
        return destinationService.getById(id)
    }

}
