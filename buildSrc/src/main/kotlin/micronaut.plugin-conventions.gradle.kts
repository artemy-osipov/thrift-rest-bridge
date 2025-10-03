plugins {
    java
}

dependencies {
    val micronautBom = "io.micronaut.platform:micronaut-platform:4.9.3"
    annotationProcessor(platform(micronautBom))
    implementation(platform(micronautBom))
    testAnnotationProcessor(platform(micronautBom))
    testImplementation("io.micronaut.test:micronaut-test-junit5")
}