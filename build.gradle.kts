plugins {
    id("com.netflix.nebula.release") version("21.0.0")
    id("com.github.ben-manes.versions") version("0.53.0")
    id("io.github.gradle-nexus.publish-plugin") version("2.0.0")
}

group = "io.github.artemy-osipov.thrift"

nexusPublishing {
    repositories {
        sonatype {
            username = System.getenv("SONATYPE_USER")
            password = System.getenv("SONATYPE_TOKEN")
            nexusUrl = uri("https://ossrh-staging-api.central.sonatype.com/service/local/")
            snapshotRepositoryUrl = uri("https://central.sonatype.com/repository/maven-snapshots/")
        }
    }
}
