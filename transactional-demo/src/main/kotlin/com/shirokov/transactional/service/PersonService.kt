package com.shirokov.transactional.service

import com.shirokov.transactional.repository.PersonRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val remotePersonService: RemotePersonService,
    private val remoteQualificationService: RemoteQualificationService,
) {
    private val log = LoggerFactory.getLogger(PersonService::class.java)

    @Transactional
    fun getPersonsFromRemote() {
        log.info("Getting persons from remote")
        val persons = personRepository.saveAll(
            remotePersonService.getPersons()
        )
        //todo do something
        persons.forEach { person ->
            person.qualification = remoteQualificationService.getPersonQualificationByIdAndPassport(
                personId = requireNotNull(person.id),
                passport = person.passport,
            )
        }
        personRepository.saveAll(persons)
        log.info("Successfully got persons from remote")
    }

}
