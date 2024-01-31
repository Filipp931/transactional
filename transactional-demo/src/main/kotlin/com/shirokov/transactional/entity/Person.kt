package com.shirokov.transactional.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Person(
    val name: String,
    val passport: String,
) {
    @Id
    @GeneratedValue
    val id: UUID? = null

    @Enumerated(EnumType.STRING)
    var qualification: PersonQualification? = null
}
