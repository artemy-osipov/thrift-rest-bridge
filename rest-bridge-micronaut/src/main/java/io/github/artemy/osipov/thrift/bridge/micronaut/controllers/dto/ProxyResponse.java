package io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProxyResponse {

    private Object response;
}
