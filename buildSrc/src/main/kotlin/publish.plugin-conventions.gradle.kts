plugins {
    java
    signing
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name = project.name
                description = "Thrift-Rest bridge for proxy thrift services"
                url = "https://github.com/artemy-osipov/thrift-rest-bridge"
                licenses {
                    license {
                        name = "MIT"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "artemy-osipov"
                        name = "Artemy Osipov"
                        email = "osipov.artemy@gmail.com"
                    }
                }
                scm {
                    connection = "git@github.com:artemy-osipov/thrift-rest-bridge.git"
                    developerConnection = "git@github.com:artemy-osipov/thrift-rest-bridge.git"
                    url = "https://github.com/artemy-osipov/thrift-rest-bridge"
                }
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_KEY")
    val signingPassword = System.getenv("GPG_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}
