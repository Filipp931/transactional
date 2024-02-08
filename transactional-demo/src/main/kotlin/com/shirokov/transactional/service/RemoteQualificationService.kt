package com.shirokov.transactional.service

import com.shirokov.transactional.entity.PersonQualification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException
import java.util.UUID

@Service
class RemoteQualificationService {

    private var error = false

    fun getPersonQualificationByIdAndPassport(
        personId: UUID,
        passport: String,
    ): PersonQualification {
        if (error) {
            throw HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE)
        }
        return PersonQualification.entries.random()
    }

    fun changeResponse() {
        error = !error
    }
}
