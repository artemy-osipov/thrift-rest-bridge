plugins {
    id("java.plugin-conventions")
    id("spring.plugin-conventions")
}

dependencies {
    implementation(project(":examples:thrift"))
    implementation(project(":rest-bridge-spring-boot-starter"))
}
