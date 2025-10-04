package io.github.artemy.osipov.thrift.bridge.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("bridge.thrift")
public class BridgeProperties {

    private String scanPackage;
    private ThriftModel artifact;

    @Data
    public static class ThriftModel {
        private List<String> repositories;
        private String groupId;
        private String artifactId;
    }
}
