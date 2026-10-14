import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()

	// Spring Snapshots
	maven {
		url = uri("https://repo.spring.io/snapshot")
		mavenContent {
			snapshotsOnly()
		}
	}

	// Spring Milestones
	maven {
		url = uri("https://repo.spring.io/milestone")
		mavenContent {
			releasesOnly()
		}
	}
}

dependencies {
	// BOM de Spring AI
	implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0"))

	// Starter de Spring AI para OpenAI
	implementation("org.springframework.ai:spring-ai-openai")
	//implementation("com.theokanning.openai-gpt3-java:client:0.11.1")


	// Swagger 2 con Springfox
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	// Hibernate & Logging
	implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")
	implementation("org.jboss.logging:jboss-logging:3.4.0.Final")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

	// Database
	implementation("org.postgresql:postgresql:42.5.0")
	runtimeOnly("com.h2database:h2")

	// JUnit
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Validations
	implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

	//Test
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<BootRun>("bootRun") {
	val envFile = File(rootProject.projectDir, ".env")
	if (envFile.exists()) {
		envFile.readLines()
			.map { it.trim() }
			.filter { it.isNotEmpty() && !it.startsWith("#") && it.contains('=') }
			.forEach { line ->
				val key = line.substringBefore('=')
				val value = line.substringAfter('=')
				if (value.isNotEmpty() && environment[key] == null) {
					environment(key, value)
				}
			}
	}
}
