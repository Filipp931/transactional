package com.shirokov.transactional.controller

import com.shirokov.transactional.service.PersonService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val personService: PersonService,
) {

    @GetMapping("/get-users")
    fun getUsers() = personService.getPersonsFromRemote()

}