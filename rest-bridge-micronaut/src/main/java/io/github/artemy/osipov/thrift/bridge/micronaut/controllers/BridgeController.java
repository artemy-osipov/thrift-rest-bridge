package io.github.artemy.osipov.thrift.bridge.micronaut.controllers;

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException;
import io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto.ProxyRequest;
import io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto.ProxyResponse;
import io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto.Service;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BridgeController {

    private final TServiceRepository serviceRepository;
    private final BridgeFacade bridgeFacade;

    @Get("/services")
    public Collection<Service> services() {
        return serviceRepository.list()
                .stream()
                .map(ModelMapper.INSTANCE::map)
                .collect(Collectors.toList());
    }

    @Get("/services/{serviceId}")
    public Service findService(@PathVariable String serviceId) {
        return ModelMapper.INSTANCE.map(
                serviceRepository.findById(serviceId)
        );
    }

    @Post("/services/{serviceId}/operations/{operationName}")
    public Object proxy(
            @PathVariable String serviceId,
            @PathVariable String operationName,
            @Valid @Body ProxyRequest request) {
        TOperation operation = serviceRepository.findById(serviceId)
                .operation(operationName);

        Object resp = bridgeFacade.proxy(operation, request.getEndpoint(), request.getBody().toString());

        if (resp == null) {
            return "";
        } else if (isPrimitive(resp.getClass())) {
            return new ProxyResponse(resp);
        } else {
            return resp;
        }
    }

    private boolean isPrimitive(Class<?> type) {
        return type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }

    @Get("/services/{serviceId}/operations/{operationName}/template")
    public String getTemplate(
            @PathVariable String serviceId,
            @PathVariable String operationName,
            @Positive @QueryValue(defaultValue = "3") int depth) {
        TOperation operation = serviceRepository.findById(serviceId)
                .operation(operationName);

        return bridgeFacade.template(operation, depth);
    }

    @Error(exception = NotFoundException.class)
    public HttpResponse<Void> processNoEntityFound() {
        return HttpResponse.notFound();
    }
}
