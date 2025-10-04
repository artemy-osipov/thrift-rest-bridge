# Thrift-REST Bridge

![Maven Central](https://img.shields.io/maven-central/v/io.github.artemy-osipov.thrift/rest-bridge-core)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e8bc02fc48c344378c5691998554fe48)](https://app.codacy.com/app/osipov.artemy/thrift-rest-bridge?utm_source=github.com&utm_medium=referral&utm_content=artemy-osipov/thrift-rest-bridge&utm_campaign=Badge_Grade_Dashboard)

The bridge for accessing thrift services using simple JSON. It allows request thrift services via REST client like Postman / Insomnia / cURL

## Usage

Add and configure starter for your project for adding thrift-rest api

### Dependency
```groovy
repositories {
  mavenCentral()
}

dependencies {
  // ...
  implementation 'io.github.artemy.osipov.thrift:rest-bridge-micronaut:0.1.0' // for micronaut project
  implementation 'io.github.artemy.osipov.thrift:rest-bridge-spring-boot-starter:0.1.0' // for spring project
  // ...
}
```

### Configuration properties

```yaml
bridge.thrift:
  # package that will be scanned for thrift services
  scanPackage: org.example.thrift
  # thrift model artifact
  artifact:
    repositories:
      - https://repo1.maven.org/maven2/
    groupId: org.example
    artifactId: thrift-api
```

## Rest API

* List services: `GET /services`
* Show service: `GET /services/:serviceId`
* Proxy operation: `POST /services/:serviceId/operations/:operationName`
* Get operation template: `GET /services/:serviceId/operations/:operationName/template`
* Get thrift artifact info: `GET /thrift-artifact`
* Fetch the latest thrift artifact: `POST /thrift-artifact/reload`
