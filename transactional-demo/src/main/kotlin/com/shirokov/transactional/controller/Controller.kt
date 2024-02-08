package com.shirokov.transactional.controller

import com.shirokov.transactional.service.PersonService
import com.shirokov.transactional.service.RemoteQualificationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val personService: PersonService,
    private val remoteQualificationService: RemoteQualificationService,
) {

    @PostMapping("/update-users")
    fun updateUsers() = personService.getPersonsFromRemote()

    @PostMapping("/update-users-transactional")
    fun updateUsersTransactional() = personService.getPersonsFromRemoteWithTransactional()

    @PostMapping("/get-users/error")
    fun changeResponse() = remoteQualificationService.changeResponse()

    @PostMapping("/reset")
    fun reset() = personService.reset()
}
