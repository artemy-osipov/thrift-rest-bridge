pluginManagement {
	repositories {
		gradlePluginPortal()
	}
}
rootProject.name = "thrift-rest-bridge"

include("rest-bridge-core")
include("rest-bridge-spec")
include("rest-bridge-micronaut")
include("rest-bridge-spring-boot-starter")
include(":examples:thrift")
include(":examples:spring-boot-app")
include(":examples:micronaut-app")
