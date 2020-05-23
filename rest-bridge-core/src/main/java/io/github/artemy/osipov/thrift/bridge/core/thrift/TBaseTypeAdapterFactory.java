package io.github.artemy.osipov.thrift.bridge.core.thrift;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.apache.thrift.TBase;

public class TBaseTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        if (TBase.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new TBaseTypeAdapter((TypeAdapter<TBase<?, ?>>) delegate);
        }

        return delegate;
    }
}