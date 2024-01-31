package com.shirokov.transactional.repository

import com.shirokov.transactional.entity.Person
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PersonRepository : JpaRepository<Person, UUID>
