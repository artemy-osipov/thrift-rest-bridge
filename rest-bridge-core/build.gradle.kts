plugins {
    `java-library`
    id("java.plugin-conventions")
    id("publish.plugin-conventions")
}

dependencies {
    api("org.apache.thrift:libthrift:0.20.0")
    implementation("org.reflections:reflections:0.10.2")
    implementation("io.github.artemy-osipov.thrift:jackson-datatype-thrift:0.4.3")

    testImplementation(project(":examples:thrift"))
    testImplementation("org.mockito:mockito-core:5.14.1")
}
