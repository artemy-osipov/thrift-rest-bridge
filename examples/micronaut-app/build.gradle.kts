plugins {
    id("java.plugin-conventions")
    id("micronaut.plugin-conventions")
}

dependencies {
    runtimeOnly("ch.qos.logback:logback-classic:1.5.8")

    implementation(project(":examples:thrift"))
    implementation(project(":rest-bridge-micronaut"))

    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation("io.micronaut:micronaut-http-client")
}
