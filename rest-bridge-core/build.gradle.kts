plugins {
    `java-library`
    id("java.plugin-conventions")
    id("publish.plugin-conventions")
}

dependencies {
    api("org.apache.thrift:libthrift:0.20.0")
    implementation("io.github.classgraph:classgraph:4.8.181")
    implementation("org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-depchain:3.3.4")
    implementation("io.github.artemy-osipov.thrift:jackson-datatype-thrift:0.4.3")

    testImplementation(project(":examples:thrift"))
    testImplementation("org.mockito:mockito-core:5.14.1")
}
