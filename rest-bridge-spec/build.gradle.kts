plugins {
    `java-library`
    id("java.plugin-conventions")
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:6.0.0")
    implementation("io.rest-assured:rest-assured:5.5.6")
    api("org.mockito:mockito-core:5.20.0")

    implementation(project(":rest-bridge-core"))
    implementation(project(":examples:thrift"))
}
