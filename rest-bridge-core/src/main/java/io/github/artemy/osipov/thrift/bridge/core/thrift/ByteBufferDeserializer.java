package io.github.artemy.osipov.thrift.bridge.core.thrift;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Base64;

public class ByteBufferDeserializer implements JsonDeserializer<ByteBuffer> {

    @Override
    public ByteBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        byte[] bytes = Base64.getDecoder().decode(json.getAsString());
        return ByteBuffer.wrap(bytes);
    }
}