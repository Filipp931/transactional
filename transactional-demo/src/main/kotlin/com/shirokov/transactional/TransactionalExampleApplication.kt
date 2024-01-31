package com.shirokov.transactional

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TransactionalExampleApplication

fun main(vararg args: String) {
    SpringApplication.run(TransactionalExampleApplication::class.java, *args)
}
