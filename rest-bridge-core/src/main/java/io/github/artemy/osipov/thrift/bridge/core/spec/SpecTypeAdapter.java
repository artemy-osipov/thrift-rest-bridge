package io.github.artemy.osipov.thrift.bridge.core.spec;

import org.apache.thrift.TBase;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.meta_data.FieldMetaData;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.withName;
import static org.reflections.ReflectionUtils.withParametersCount;

public class SpecTypeAdapter {

    private final Map<Class<? extends TBase>, SpecType> recursiveCache = new ConcurrentHashMap<>();

    public SpecType from(Type type) {
        Class<?> clazz;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            clazz = (Class<?>) parameterizedType.getRawType();
            if (Collection.class.isAssignableFrom(clazz)
                    && parameterizedType.getActualTypeArguments().length == 1) {
                return SpecType.array(from((Class<?>) parameterizedType.getActualTypeArguments()[0]));
            }
        } else if (type instanceof Class) {
            clazz = (Class<?>) type;
        } else {
            throw new IllegalArgumentException("Can't process type - " + type);
        }

        return from(clazz);
    }

    public SpecType from(Class<?> clazz) {
        if (isAssignable(clazz, Number.class, byte.class, short.class, int.class, long.class, float.class, double.class)) {
            return SpecType.primitive(DataType.NUMBER);
        }
        if (isAssignable(clazz, Enum.class)) {
            return SpecType.primitive(DataType.ENUM);
        }
        if (isAssignable(clazz, String.class, byte[].class, ByteBuffer.class)) {
            return SpecType.primitive(DataType.STRING);
        }
        if (isAssignable(clazz, Boolean.class, boolean.class)) {
            return SpecType.primitive(DataType.BOOLEAN);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return SpecType.array(SpecType.object());
        }
        if (TBase.class.isAssignableFrom(clazz)) {
            if (recursiveCache.containsKey(clazz)) {
                return recursiveCache.get(clazz);
            }
            return fromThrift((Class<? extends TBase>) clazz);
        }

        throw new IllegalArgumentException("Can't find corresponding spec type for - " + clazz);
    }

    private SpecType fromThrift(Class<? extends TBase> clazz) {
        Map<String, Type> fields = extractFields(clazz);
        SpecField[] specFields = new SpecField[fields.size()];
        SpecType specType = SpecType.object(specFields);
        recursiveCache.put(clazz, specType);

        int i = 0;
        for (Map.Entry<String, Type> field : fields.entrySet()) {
            specFields[i] = new SpecField(field.getKey(), from(field.getValue()));
            i++;
        }

        return specType;
    }

    private boolean isAssignable(Class<?> clazz, Class<?>... baseClasses) {
        return Arrays.stream(baseClasses)
                .anyMatch(baseClass -> baseClass.isAssignableFrom(clazz));
    }

    private Map<String, Type> extractFields(Class<? extends TBase> clazz) {
        List<String> fieldNames = FieldMetaData.getStructMetaDataMap(clazz)
                .keySet()
                .stream()
                .map(TFieldIdEnum::getFieldName)
                .collect(Collectors.toList());

        Map<String, Type> fields = new LinkedHashMap<>();
        for (String fieldName : fieldNames) {
            Optional<Type> type = getFieldTypeByGetter(clazz, fieldName);

            type.ifPresent(value -> fields.put(fieldName, value));
        }

        return fields;
    }

    private Optional<Type> getFieldTypeByGetter(Class<?> clazz, String field) {
        return ReflectionUtils.getMethods(clazz,
                withName("get" + capitalize(field))
                        .or(withName("is" + capitalize(field))),
                withParametersCount(0)
        )
                .stream()
                .findFirst()
                .map(Method::getGenericReturnType);
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
