package io.github.artemy.osipov.thrift.bridge.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecField;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecType;
import io.github.artemy.osipov.thrift.bridge.core.thrift.ByteBufferDeserializer;
import io.github.artemy.osipov.thrift.bridge.core.thrift.TBaseTypeAdapterFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Arrays;

@RequiredArgsConstructor
public class TArguments {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ByteBuffer.class, new ByteBufferDeserializer())
            .registerTypeAdapterFactory(new TBaseTypeAdapterFactory())
            .create();

    @Getter
    private final Parameter[] parameters;

    public Object[] args(String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        return Arrays.stream(parameters)
                .map(p -> gson.fromJson(jsonObject.get(p.getName()), p.getParameterizedType()))
                .toArray(Object[]::new);
    }

    public SpecType specType() {
        return SpecType.object(
                Arrays.stream(parameters)
                        .map(p -> new SpecField(p.getName(), SpecType.of(p.getParameterizedType())))
                        .toArray(SpecField[]::new)
        );
    }
}
