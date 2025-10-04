package io.github.artemy.osipov.thrift.bridge.micronaut.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.util.List;

@Data
@Introspected
@ConfigurationProperties("bridge.thrift")
public class BridgeProperties {

    private String scanPackage;
    private ThriftModel artifact;

    @Data
    @Introspected
    @ConfigurationProperties("artifact")
    public static class ThriftModel {
        private List<String> repositories;
        private String groupId;
        private String artifactId;
    }
}
