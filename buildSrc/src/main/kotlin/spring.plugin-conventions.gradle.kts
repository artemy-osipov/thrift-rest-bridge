plugins {
    java
}

dependencies {
    val springBootBom = "org.springframework.boot:spring-boot-dependencies:3.5.6"
    implementation(platform(springBootBom))
    annotationProcessor(platform(springBootBom))
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}