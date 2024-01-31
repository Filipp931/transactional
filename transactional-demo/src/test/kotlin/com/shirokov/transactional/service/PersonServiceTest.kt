package com.shirokov.transactional.service

import com.ninjasquad.springmockk.MockkClear
import com.ninjasquad.springmockk.SpykBean
import com.ninjasquad.springmockk.clear
import com.shirokov.transactional.repository.PersonRepository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import jakarta.transaction.Transactional
import kotlin.math.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.support.TransactionTemplate

@SpringBootTest
class PersonServiceTest {

    @Autowired
    lateinit var personService: PersonService

    @Autowired
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var remotePersonService: RemotePersonService

    @SpykBean
    lateinit var remoteQualificationService: RemoteQualificationService

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    @BeforeEach
    fun clearDb() {
        personRepository.deleteAll()
    }

    @Test
    fun `should save all persons without exception`() {
        personService.getPersonsFromRemote()
        assertEquals(remotePersonService.getPersons().size, personRepository.findAll().size)
    }

    @Test
    fun `should save all persons with exception`() {
        every { remoteQualificationService.getPersonQualificationByIdAndPassport(any(), any()) } throws Exception()
        assertThrows<Exception> { personService.getPersonsFromRemote() }
    }

    @Test
    fun `should fail with exception on retry`() {
        every { remoteQualificationService.getPersonQualificationByIdAndPassport(any(), any()) } throws Exception()
        try {
            personService.getPersonsFromRemote()
        } catch (e: Exception) {
            assertTrue(personRepository.findAll().isNotEmpty())
        }
        clearMocks(remoteQualificationService)

        assertThrows<RuntimeException> { personService.getPersonsFromRemote() }
    }

    @Test
    fun `should not fail with exception on transactional retry`() {
        every { remoteQualificationService.getPersonQualificationByIdAndPassport(any(), any()) } throws Exception()
        try {
            transactionTemplate.executeWithoutResult {
                personService.getPersonsFromRemote()
            }
        } catch (e: Exception) {
            assertFalse(personRepository.findAll().isNotEmpty())
        }

        clearMocks(remoteQualificationService)

        assertDoesNotThrow { personService.getPersonsFromRemote() }
    }

}
