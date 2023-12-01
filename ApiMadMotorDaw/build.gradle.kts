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
    //Dependecias de Spring WEB
    implementation("org.springframework.boot:spring-boot-starter-web")
    //Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    //Spring test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    //Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //H2 base de datos
    implementation("com.h2database:h2")
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
    //MongoDB JPA
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    //Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    //JWT
    implementation("com.auth0:java-jwt:4.4.0")
    //Test Security
    testImplementation("org.springframework.security:spring-security-test")


}

tasks.withType<Test> {
    useJUnitPlatform()
}

/*
* Esto intenta encontrar la propiedad llamada spring.profiles.active en el proyecto.
* Si la encuentra, usa ese valor. Si no encuentra la propiedad, establece el valor predeterminado en "dev".
* Esto permite que la propiedad del sistema sea configurada por medio de la línea de comandos al ejecutar las pruebas o, de lo contrario,
* usa "dev" como perfil por defecto.
* */

tasks.test {
    systemProperty("spring.profiles.active", project.findProperty("spring.profiles.active") ?: "dev")
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
