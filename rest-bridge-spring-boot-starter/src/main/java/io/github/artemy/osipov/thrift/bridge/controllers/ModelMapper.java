package io.github.artemy.osipov.thrift.bridge.controllers;

import io.github.artemy.osipov.thrift.bridge.controllers.dto.ServiceDTO;
import io.github.artemy.osipov.thrift.bridge.core.TService;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import org.apache.thrift.TServiceClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, imports = TServiceClient.class)
public interface ModelMapper {

    @Mapping(expression = "java(map(src.getOperations().values()))", target = "operations")
    ServiceDTO map(TService src);

    List<ServiceDTO.OperationDTO> map(Collection<TOperation> operation);

    ServiceDTO.OperationDTO map(TOperation operation);
}
