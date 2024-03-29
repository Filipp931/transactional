pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val gradleDependencyManagementVersion: String by settings
    val detektVersion: String by settings
    val ktlintVersion: String by settings
    val gitPropertiesVersion: String by settings
    val palantirGitVersion: String by settings
    val jGitVerVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.jpa") version kotlinVersion apply false
        `maven-publish`
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version gradleDependencyManagementVersion apply false
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion apply false
        id("com.palantir.git-version") version palantirGitVersion apply false
        id("com.gorylenko.gradle-git-properties") version gitPropertiesVersion apply false
        id("fr.brouillard.oss.gradle.jgitver") version jGitVerVersion
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "transactional"

include(
    "transactional-demo",
)
