package io.github.artemy.osipov.thrift.bridge.core.spec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TemplateSpec {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public String format(SpecType type) {
        return format(type, Integer.MAX_VALUE);
    }

    public String format(SpecType type, int depth) {
        return gson.toJson(
                element(type, depth)
        );
    }

    private JsonElement element(SpecType type, int depth) {
        if (depth == 0) {
            return JsonNull.INSTANCE;
        }

        switch (type.getType()) {
            case BOOLEAN:
                return new JsonPrimitive(false);
            case NUMBER:
                return new JsonPrimitive(0);
            case STRING:
                return new JsonPrimitive("");
            case ARRAY:
                return array(type.getContainerType(), depth);
            case OBJECT:
                return object(type.getNested(), depth);
            default:
                return JsonNull.INSTANCE;
        }
    }

    private JsonArray array(SpecType containerType, int depth) {
        var node = new JsonArray(1);
        node.add(element(containerType, depth - 1));
        return node;
    }

    private JsonObject object(SpecField[] fields, int depth) {
        var node = new JsonObject();
        for (SpecField field : fields) {
            node.add(field.getName(), element(field.getType(), depth - 1));
        }
        return node;
    }
}
