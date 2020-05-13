package io.github.artemy.osipov.thrift.bridge.micronaut.controllers;

import io.github.artemy.osipov.thrift.bridge.core.TService;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto.Service;
import org.apache.thrift.TServiceClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, imports = TServiceClient.class)
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    @Mapping(expression = "java(map(src.getOperations().values()))", target = "operations")
    Service map(TService src);

    List<Service.Operation> map(Collection<TOperation> operation);

    Service.Operation map(TOperation operation);
}
