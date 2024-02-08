package com.shirokov.transactional.service

import com.shirokov.transactional.entity.Person
import org.springframework.stereotype.Service

@Service
class RemotePersonService {

    fun getPersons() = mutableListOf(
        Person(
            name = "Ivan",
            passport = "passport",
        ),
        Person(
            name = "Olga",
            passport = "passport1",
        ),
        Person(
            name = "Anna",
            passport = "passport2",
        ),
    )
}
