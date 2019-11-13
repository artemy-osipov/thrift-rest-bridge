package io.github.artemy.osipov.thrift.bridge.domain;

import io.github.artemy.osipov.thrift.bridge.domain.exception.NotFoundException;
import lombok.*;

import java.lang.reflect.Method;
import java.util.Arrays;

@Data
@ToString(exclude = "service")
@EqualsAndHashCode(exclude = "service")
public class TOperation {

    private final String name;

    @Setter(AccessLevel.PACKAGE)
    private TService service;

    public Method buildClientMethod(Class<?> clientClass) {
        return Arrays.stream(clientClass.getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No method %s in client %s", name, clientClass.getName())));
    }
}
