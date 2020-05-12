package io.github.artemy.osipov.thrift.bridge.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.ProxyRequestDTO;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.ServiceDTO;
import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BridgeController {

    private final ModelMapper modelMapper = Mappers.getMapper(ModelMapper.class);
    private final TServiceRepository serviceRepository;
    private final BridgeFacade bridgeFacade;
    private final ObjectMapper objectMapper;

    @GetMapping("/services")
    public Collection<ServiceDTO> services() {
        return serviceRepository.list()
                .stream()
                .map(modelMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/services/{serviceId}")
    public ServiceDTO findService(@PathVariable String serviceId) {
        return modelMapper.map(
                serviceRepository.findById(serviceId)
        );
    }

    @SneakyThrows
    @PostMapping("/services/{serviceId}/operations/{operationName}")
    public Object proxy(
            @PathVariable String serviceId,
            @PathVariable String operationName,
            @Valid @RequestBody ProxyRequestDTO request) {
        TOperation operation = serviceRepository.findById(serviceId)
                .operation(operationName);

        return bridgeFacade.proxy(operation, request.getEndpoint(), objectMapper.writeValueAsString(request.getBody()));
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
}
