plugins {
    `java-library`
    id("java.plugin-conventions")
    id("publish.plugin-conventions")
    id("micronaut.plugin-conventions")
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-inject-java")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")

    api("io.micronaut:micronaut-inject")
    api("io.micronaut:micronaut-http-server-netty")
    api("io.micronaut:micronaut-jackson-databind")
    api("io.micronaut.validation:micronaut-validation")

    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    implementation("org.mapstruct:mapstruct:1.6.3")

    implementation("io.github.artemy-osipov.thrift:jackson-datatype-thrift:0.4.3")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    runtimeOnly("org.yaml:snakeyaml")

    api(project(":rest-bridge-core"))

    testImplementation("io.micronaut:micronaut-inject-groovy")
    testImplementation(project(":rest-bridge-spec"))
}
