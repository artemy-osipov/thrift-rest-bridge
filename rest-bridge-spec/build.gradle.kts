plugins {
    `java-library`
    id("java.plugin-conventions")
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.11.2")
    implementation("io.rest-assured:rest-assured:5.5.0")
    api("org.mockito:mockito-core:5.14.1")

    implementation(project(":rest-bridge-core"))
    implementation(project(":examples:thrift"))
}
