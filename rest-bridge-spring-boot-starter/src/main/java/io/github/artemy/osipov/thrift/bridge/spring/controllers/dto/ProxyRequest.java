package io.github.artemy.osipov.thrift.bridge.spring.controllers.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProxyRequest {

    @NotBlank
    private String endpoint;
    private String body;
}
