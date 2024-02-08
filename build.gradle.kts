import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val kotlinVersion: String by project
val springBootVersion: String by project
val detektVersion: String by project

val h2Version: String by project
val flywayVersion: String by project

val openApiVersion: String by project

val logstashLogbackEncoderVersion: String by project
val slf4jKotlinExtensionVersion: String by project

val testContainers: String by project
val junitPlatformLauncherVersion: String by project
val mockkVersion: String by project
val springMockkVersion: String by project

plugins {
	kotlin("jvm") apply false
	kotlin("plugin.spring") apply false
	kotlin("plugin.jpa") apply false
	id("org.springframework.boot") apply false
	id("io.spring.dependency-management") apply false
	id("org.jlleitschuh.gradle.ktlint") apply false
	id("com.palantir.git-version") apply false
	id("com.gorylenko.gradle-git-properties") apply false

	id("fr.brouillard.oss.gradle.jgitver")
	`maven-publish`
}

allprojects {
	group = "com.shirokov.examples"
	version = "1.0.0-SNAPSHOT"

	repositories {
		mavenCentral()
		mavenLocal()
	}
}

subprojects {
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "kotlin")
	apply(plugin = "org.jlleitschuh.gradle.ktlint")
	apply(plugin = "maven-publish")

	tasks.withType<Test> {
		useJUnitPlatform()
		testLogging {
			events = setOf(
				TestLogEvent.FAILED,
				TestLogEvent.PASSED,
				TestLogEvent.SKIPPED
			)
			exceptionFormat = TestExceptionFormat.FULL
			showExceptions = true
			showCauses = true
			showStackTraces = true
		}
	}


	the<DependencyManagementExtension>().apply {
		imports {
			mavenBom("org.testcontainers:testcontainers-bom:$testContainers")
			mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion") {
				bomProperty("kotlin.version", kotlinVersion)
			}
		}
		dependencies {
			dependency("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

			dependency("com.h2database:h2:$h2Version")
			dependency("org.flywaydb:flyway-core:$flywayVersion")

			dependency("org.springdoc:springdoc-openapi-starter-webmvc-ui:$openApiVersion")

			dependency("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")
			dependency("com.frimastudio:slf4j-kotlin-extensions:$slf4jKotlinExtensionVersion")

			dependency("org.junit.platform:junit-platform-launcher:$junitPlatformLauncherVersion")
			dependency("io.mockk:mockk:$mockkVersion")
			dependency("com.ninja-squad:springmockk:$springMockkVersion")

		}
	}

	tasks.withType<KotlinCompile>().configureEach {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_17.toString()
		}
	}


	val sourcesJar by tasks.creating(Jar::class) {
		group = JavaBasePlugin.DOCUMENTATION_GROUP
		description = "Assembles sources JAR"
		archiveClassifier.set("sources")
		from(project.the<SourceSetContainer>()["main"].allSource)
	}

	publishing {
		publications {
			create<MavenPublication>("maven") {
				versionMapping {
					usage("java-api") {
						fromResolutionOf("runtimeClasspath")
					}
					usage("java-runtime") {
						fromResolutionResult()
					}
				}
				from(components["java"])
				afterEvaluate {
					artifact(sourcesJar)
				}
			}
		}
	}

	afterEvaluate {
		if (tasks.withType<BootJar>().isNotEmpty()) {
			tasks.withType<BootJar> {
				layered {}
			}

			val bootJar: BootJar by tasks
			bootJar.apply {
				archiveClassifier.set("application")
			}

			val explodeJar by tasks.register<JavaExec>("explodeJar") {
				group = "build"
				description = "Explodes layered fat jar into build/libs"

				dependsOn(bootJar)

				classpath(fileTree(bootJar.archiveFile))
				workingDir(mkdir(bootJar.destinationDirectory.dir("exploded")))
				jvmArgs("-Djarmode=layertools")
				args("extract")
				outputs.dir(bootJar.destinationDirectory.dir("exploded"))
			}

		}
	}
}


