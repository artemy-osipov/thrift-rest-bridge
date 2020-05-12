package io.github.artemy.osipov.thrift.bridge.spring.controllers.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProxyRequestDTO {

    @NotBlank
    private String endpoint;
    private JsonNode body;
}
