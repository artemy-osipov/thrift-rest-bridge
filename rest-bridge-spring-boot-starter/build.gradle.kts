plugins {
    `java-library`
    id("java.plugin-conventions")
    id("publish.plugin-conventions")
    id("spring.plugin-conventions")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    api(project(":rest-bridge-core"))

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("io.github.artemy-osipov.thrift:jackson-datatype-thrift:0.4.3")

    testImplementation(project(":examples:thrift"))
    testImplementation(project(":rest-bridge-spec"))
}
