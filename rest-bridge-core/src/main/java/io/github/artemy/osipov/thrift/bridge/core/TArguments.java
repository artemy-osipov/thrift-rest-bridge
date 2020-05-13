package io.github.artemy.osipov.thrift.bridge.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecField;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

@RequiredArgsConstructor
public class TArguments {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ByteBuffer.class, new ByteBufferDeserializer())
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

    private static class ByteBufferDeserializer implements JsonDeserializer<ByteBuffer> {

        @Override
        public ByteBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            byte[] bytes = Base64.getDecoder().decode(json.getAsString());
            return ByteBuffer.wrap(bytes);
        }
    }
}