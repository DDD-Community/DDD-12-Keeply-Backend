plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    mainClass.set("com.keeply.KeeplyApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("io.projectreactor:reactor-test")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    testImplementation("org.springframework.security:spring-security-test")


    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Google Cloud Vision API
    implementation("com.google.cloud:google-cloud-vision:3.31.0")

    // Google Cloud Natural Language API (v2)
    implementation("com.google.cloud:google-cloud-language:2.64.0")

    implementation("com.h2database:h2")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.121.Final:osx-x86_64")

    implementation("software.amazon.awssdk:s3:2.25.48")
    implementation("software.amazon.awssdk:lambda:2.25.48")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    //MockMultipart를 사용하기 위함.
    implementation("org.springframework:spring-test")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.google.firebase:firebase-admin:9.2.0")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runDetectText") {
    group = "application"
    description = "Run the DetectText main class"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.keeply.ocrtest.DetectText")

    environment("GOOGLE_APPLICATION_CREDENTIALS", "/Users/jihwan/Downloads/cobalt-infinity-464512-u8-3f518df4f484.json")
}

tasks.register<JavaExec>("runDetectLabels") {
    group = "application"
    description = "Run the DetectLabels main class"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.keeply.ocrtest.DetectLabels")

    environment("GOOGLE_APPLICATION_CREDENTIALS", "/Users/jihwan/Downloads/cobalt-infinity-464512-u8-3f518df4f484.json")
}

