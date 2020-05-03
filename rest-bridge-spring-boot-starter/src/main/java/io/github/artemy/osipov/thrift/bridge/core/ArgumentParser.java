package io.github.artemy.osipov.thrift.bridge.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

@Component
public class ArgumentParser {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ByteBuffer.class, new ByteBufferDeserializer())
            .create();

    public Object[] parse(Parameter[] types, String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        return Arrays.stream(types)
                .map(p -> gson.fromJson(jsonObject.get(p.getName()), p.getType()))
                .toArray(Object[]::new);
    }

    static class ByteBufferDeserializer implements JsonDeserializer<ByteBuffer> {

        @Override
        public ByteBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            byte[] bytes = Base64.getDecoder().decode(json.getAsString());
            return ByteBuffer.wrap(bytes);
        }
    }
}
