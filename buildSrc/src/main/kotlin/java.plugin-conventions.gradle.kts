plugins {
    java
    groovy
}

group = rootProject.group
java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation("org.apache.groovy:groovy-test:4.0.23")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
    }
    test {
        useJUnitPlatform()
    }
}
