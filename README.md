# Thrift-REST Bridge

[ ![Download](https://api.bintray.com/packages/aosipov/oss/rest-bridge-core/images/download.svg) ](https://bintray.com/aosipov/oss/rest-bridge-core/_latestVersion)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e8bc02fc48c344378c5691998554fe48)](https://app.codacy.com/app/osipov.artemy/thrift-rest-bridge?utm_source=github.com&utm_medium=referral&utm_content=artemy-osipov/thrift-rest-bridge&utm_campaign=Badge_Grade_Dashboard)

The bridge for accessing thrift services using simple JSON. It allows request thrift services via REST client like Postman / Insomnia / cURL

## Usage

Add and configure starter for your project for adding thrift-rest api

### Dependency
```groovy
repositories {
  jcenter()
}

dependencies {
  // ...
  implementation 'org.example:thrift-api:0.2.+' // dependency with thrift services/entity
  implementation 'io.github.artemy.osipov.thrift:rest-bridge-micronaut:0.1.0' // for micronaut project
  implementation 'io.github.artemy.osipov.thrift:rest-bridge-spring-boot-starter:0.1.0' // for spring project
  // ...
}
```

### Configuration properties

```yaml
# package that will be scanned for thrift services
bridge.thrift.scanPackage: org.example.thrift
```

## Rest API

* List services: `GET /services`
* Show service: `GET /services/:serviceId`
* Proxy operation: `POST /services/:serviceId/operations/:operationName`
* Get operation template: `GET /services/:serviceId/operations/:operationName/template`
