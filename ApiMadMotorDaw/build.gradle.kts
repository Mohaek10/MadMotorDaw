plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.madmotor"
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
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    //Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //H2 base de datos
    runtimeOnly("com.h2database:h2")
    //Validacion
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    //Jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    //Pasar a xml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    //OpenApi o swagger para generacion de documentacion
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
