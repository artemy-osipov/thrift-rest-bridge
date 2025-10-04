package io.github.artemy.osipov.thrift.bridge.spring.controllers;

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import io.github.artemy.osipov.thrift.bridge.core.ThriftModelArtifactRepository;
import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.ProxyRequest;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.ProxyResponse;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.Service;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.ThriftArtifact;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class BridgeController {

    private final BridgeFacade bridgeFacade;
    private final TServiceRepository serviceRepository;
    private final ThriftModelArtifactRepository modelArtifactRepository;

    @PostConstruct
    public void init() {
        reloadThriftModel();
    }

    @GetMapping("/services")
    public Collection<Service> services() {
        return serviceRepository.list()
                .stream()
                .map(ModelMapper.INSTANCE::map)
                .toList();
    }

    @GetMapping("/services/{serviceId}")
    public Service findService(@PathVariable String serviceId) {
        return ModelMapper.INSTANCE.map(
                serviceRepository.findById(serviceId)
        );
    }

    @PostMapping("/services/{serviceId}/operations/{operationName}")
    public Object proxy(
            @PathVariable String serviceId,
            @PathVariable String operationName,
            @Valid @RequestBody ProxyRequest request) {
        TOperation operation = serviceRepository.findById(serviceId)
                .operation(operationName);

        Object resp = bridgeFacade.proxy(operation, request.getEndpoint(), request.getBody().toString());

        if (resp == null || !isPrimitive(resp.getClass())) {
            return resp;
        } else {
            return new ProxyResponse(resp);
        }
    }

    private boolean isPrimitive(Class<?> type) {
        return ClassUtils.isPrimitiveOrWrapper(type) || type == String.class;
    }

    @GetMapping("/services/{serviceId}/operations/{operationName}/template")
    public String getTemplate(
            @PathVariable String serviceId,
            @PathVariable String operationName,
            @Valid @Positive @RequestParam(defaultValue = "3") int depth) {
        TOperation operation = serviceRepository.findById(serviceId)
                .operation(operationName);

        return bridgeFacade.template(operation, depth);
    }

    @GetMapping("/thrift-artifact")
    public ThriftArtifact getThriftModel() {
        if (!modelArtifactRepository.hasArtifact()) {
            throw new NotFoundException();
        }
        return ModelMapper.INSTANCE.map(
                modelArtifactRepository.getArtifact(),
                modelArtifactRepository.getVersion()
        );
    }

    @PostMapping("/thrift-artifact/reload")
    public void reloadThriftModel() {
        serviceRepository.reload(modelArtifactRepository);
    }
}
