package io.github.artemy.osipov.thrift.bridge.controllers.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OperationDTO {

    private String name;
    private String uri;
}
