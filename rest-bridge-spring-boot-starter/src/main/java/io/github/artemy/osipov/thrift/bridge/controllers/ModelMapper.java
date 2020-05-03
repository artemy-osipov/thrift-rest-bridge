package io.github.artemy.osipov.thrift.bridge.controllers;

import io.github.artemy.osipov.thrift.bridge.controllers.dto.OperationDTO;
import io.github.artemy.osipov.thrift.bridge.controllers.dto.ServiceDTO;
import io.github.artemy.osipov.thrift.bridge.core.TService;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Collection;
import java.util.List;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ModelMapper {

    @Mapping(expression = "java(map(src.getOperations().values()))", target = "operations")
    ServiceDTO map(TService src);

    List<OperationDTO> map(Collection<TOperation> operation);

    default OperationDTO map(TOperation operation) {
        return new OperationDTO()
                .setName(operation.getName())
                .setUri(
                        MvcUriComponentsBuilder.fromMethodCall(on(BridgeController.class)
                                .proxy(
                                        operation.getService().getName(),
                                        operation.getName(),
                                        null,
                                        null))
                                .build().toUriString()
                );
    }
}
