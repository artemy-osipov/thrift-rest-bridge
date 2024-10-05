plugins {
    id("com.netflix.nebula.release") version("19.0.10")
    id("com.github.ben-manes.versions") version("0.51.0")
    id("io.github.gradle-nexus.publish-plugin") version("2.0.0")
}

group = "io.github.artemy-osipov.thrift"

nexusPublishing {
    repositories {
        sonatype {
            username = System.getenv("SONATYPE_USER")
            password = System.getenv("SONATYPE_TOKEN")
            nexusUrl = uri("https://s01.oss.sonatype.org/service/local/")
            snapshotRepositoryUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}
