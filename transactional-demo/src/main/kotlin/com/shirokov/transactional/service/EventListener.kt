package com.shirokov.transactional.service

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EventListener(
    private val personService: PersonService,
) {

//    @EventListener(ApplicationReadyEvent::class)
    fun test() {
        personService.getPersonsFromRemote()
    }

}