package io.github.artemy.osipov.thrift.bridge.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ThriftConverter {

    private final ObjectMapper mapper;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ByteBuffer.class, new ByteBufferDeserializer())
            .create();

    public Object[] parseArgs(Method method, JsonNode req) {
        return Arrays.stream(method.getParameters())
                .map(p -> parse(req.get(p.getName()), p.getType()))
                .toArray(Object[]::new);
    }

    @SneakyThrows
    private <T> T parse(JsonNode node, Class<T> clazz) {
        String json = mapper.writeValueAsString(node);
        return gson.fromJson(json, clazz);
    }

    static class ByteBufferDeserializer implements JsonDeserializer<ByteBuffer> {

        @Override
        public ByteBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            byte[] bytes = Base64.getDecoder().decode(json.getAsString());
            return ByteBuffer.wrap(bytes);
        }
    }
}
