plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "ca.gbc"
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
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.3")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.5")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    testImplementation("org.testcontainers:junit-jupiter:1.15.3")
    testImplementation("org.testcontainers:postgresql:1.15.3")

    implementation("io.micrometer:micrometer-observation:1.11.3")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.1.4")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave:2.16.4")

    implementation("org.springframework.boot:spring-boot-starter-logging")

    testImplementation ("org.wiremock:wiremock:3.3.1")
    testImplementation ("javax.servlet:javax.servlet-api:4.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}