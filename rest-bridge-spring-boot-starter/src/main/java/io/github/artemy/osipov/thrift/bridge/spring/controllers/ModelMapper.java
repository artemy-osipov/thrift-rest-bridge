package io.github.artemy.osipov.thrift.bridge.spring.controllers;

import io.github.artemy.osipov.thrift.bridge.core.TService;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.core.ThriftModelArtifactRepository;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.Service;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.ThriftArtifact;
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

    @Mapping(source = "artifact.groupId", target = "groupId")
    @Mapping(source = "artifact.artifactId", target = "artifactId")
    @Mapping(source = "version", target = "version")
    ThriftArtifact map(ThriftModelArtifactRepository.ThriftModelArtifact artifact, String version);
}
