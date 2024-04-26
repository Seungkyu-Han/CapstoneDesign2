import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	war
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
	kotlin("kapt") version "1.9.23"
	kotlin("plugin.serialization") version "1.5.31"
	idea
}

group = "knu"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	//jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	//QueryDsl
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("com.querydsl:querydsl-apt:5.0.0:jakarta")
	implementation("jakarta.persistence:jakarta.persistence-api")
	implementation("jakarta.annotation:jakarta.annotation-api")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	//lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	//mysql
	runtimeOnly("com.mysql:mysql-connector-j")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//jwt
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

	//json
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

idea{
	module{
		val kaptMain = file("build/generated/source/kapt/main")
		sourceDirs.add(kaptMain)
		generatedSourceDirs.add(kaptMain)
	}
}