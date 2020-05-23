package io.github.artemy.osipov.thrift.bridge.core.thrift;

import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.SneakyThrows;
import org.apache.thrift.TBase;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class TBaseTypeAdapter extends TypeAdapter<TBase<?, ?>> {

    private final TypeAdapter<TBase<?, ?>> delegate;
    private final Method peekStack;

    @SneakyThrows
    public TBaseTypeAdapter(TypeAdapter<TBase<?, ?>> delegate) {
        this.delegate = delegate;
        peekStack = JsonTreeReader.class.getDeclaredMethod("peekStack");
        peekStack.setAccessible(true);
    }

    @Override
    public void write(JsonWriter out, TBase<?, ?> value) throws IOException {
        delegate.write(out, value);
    }

    @Override
    public TBase<?, ?> read(JsonReader in) throws IOException {
        Set<String> fields = extractFields(in);
        TBase<?, ?> thrift = delegate.read(in);
        setIsSetFields(thrift, fields);
        return thrift;
    }

    @SneakyThrows
    private Set<String> extractFields(JsonReader reader) {
        JsonObject json = (JsonObject) peekStack.invoke(reader);
        return json.keySet();
    }

    private void setIsSetFields(TBase<?, ?> thrift, Set<String> fields) {
        fields.forEach(field -> setIsSetField(thrift, field));
    }

    @SneakyThrows
    private void setIsSetField(TBase<?, ?> thrift, String field) {
        String methodName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1) + "IsSet";
        Method method = thrift.getClass().getMethod(methodName, boolean.class);
        method.invoke(thrift, true);
    }
}
