package io.github.artemy.osipov.thrift.bridge.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecField;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecType;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecTypeAdapter;
import io.github.artemy.osipov.thrift.jackson.ThriftModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Parameter;
import java.util.Arrays;

@RequiredArgsConstructor
public class TArguments {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new ThriftModule());

    @Getter
    private final Parameter[] parameters;

    @SneakyThrows
    public Object[] args(String body) {
        JsonNode json = mapper.readTree(body);
        return Arrays.stream(parameters)
                .map(p -> mapper.convertValue(json.get(p.getName()), mapper.constructType(p.getParameterizedType())))
                .toArray(Object[]::new);
    }

    public SpecType specType() {
        var specTypeAdapter = new SpecTypeAdapter();
        return SpecType.object(
                Arrays.stream(parameters)
                        .map(p -> new SpecField(p.getName(), specTypeAdapter.from(p.getParameterizedType())))
                        .toArray(SpecField[]::new)
        );
    }
}
