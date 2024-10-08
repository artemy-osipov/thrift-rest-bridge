import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    `java-library`
    id("java.plugin-conventions")
    id("com.linecorp.thrift-gradle-plugin") version ("0.6.1")
}

compileThrift {
    generator("java", "generated_annotations=suppress")
    val thriftLib = if (Os.isFamily(Os.FAMILY_MAC)) {
        "thrift.osx-x86_64"
    } else {
        "thrift.linux-x86_64"
    }
    thriftExecutable(
        rootProject.layout.projectDirectory.dir("gradle/libs").file(thriftLib).asFile.path
    )
}

dependencies {
    api("org.apache.thrift:libthrift:0.20.0")
    implementation("org.slf4j:slf4j-api:2.0.12")
}
