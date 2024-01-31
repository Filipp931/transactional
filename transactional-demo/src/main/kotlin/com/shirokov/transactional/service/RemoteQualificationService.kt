package com.shirokov.transactional.service

import com.shirokov.transactional.entity.PersonQualification
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RemoteQualificationService {

    fun getPersonQualificationByIdAndPassport(
        personId: UUID,
        passport: String,
    ): PersonQualification {
        return PersonQualification.entries.random()
    }

}
