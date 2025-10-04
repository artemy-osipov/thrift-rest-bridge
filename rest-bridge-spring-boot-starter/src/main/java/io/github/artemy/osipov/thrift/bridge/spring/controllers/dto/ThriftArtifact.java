package io.github.artemy.osipov.thrift.bridge.spring.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThriftArtifact {

    private String groupId;
    private String artifactId;
    private String version;
}
