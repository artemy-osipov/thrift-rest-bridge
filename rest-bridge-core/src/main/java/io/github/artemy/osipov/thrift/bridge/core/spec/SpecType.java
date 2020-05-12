package io.github.artemy.osipov.thrift.bridge.core.spec;

import lombok.Value;
import org.apache.thrift.TBase;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.meta_data.FieldMetaData;
import org.reflections.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Collection;

@Value
public class SpecType {
    DataType type;
    SpecType containerType;
    SpecField[] nested;

    public static SpecType object(SpecField... fields) {
        return new SpecType(DataType.OBJECT, null, fields);
    }

    public static SpecType primitive(DataType type) {
        if (!type.isPrimitive()) {
            throw new IllegalArgumentException(type + " is not primitive");
        }
        return new SpecType(type, null, null);
    }

    public static SpecType array(SpecType containerType) {
        return new SpecType(DataType.ARRAY, containerType, null);
    }

    public static SpecType of(Type type) {
        Class<?> clazz;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            clazz = (Class<?>) parameterizedType.getRawType();
            if (Collection.class.isAssignableFrom(clazz)
                    && parameterizedType.getActualTypeArguments().length == 1) {
                return array(of((Class<?>) parameterizedType.getActualTypeArguments()[0]));
            }
        } else if (type instanceof Class) {
            clazz = (Class<?>) type;
        } else {
            throw new IllegalArgumentException("Can't process type - " + type);
        }

        return of(clazz);
    }

    public static SpecType of(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)
                || byte.class.isAssignableFrom(clazz)
                || short.class.isAssignableFrom(clazz)
                || int.class.isAssignableFrom(clazz)
                || long.class.isAssignableFrom(clazz)
                || double.class.isAssignableFrom(clazz)) {
            return primitive(DataType.NUMBER);
        }
        if (String.class.isAssignableFrom(clazz)
                || Enum.class.isAssignableFrom(clazz)
                || ByteBuffer.class.isAssignableFrom(clazz)) {
            return primitive(DataType.STRING);
        }
        if (Boolean.class.isAssignableFrom(clazz)
                || boolean.class.isAssignableFrom(clazz)) {
            return primitive(DataType.BOOLEAN);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return array(object());
        }
        if (TBase.class.isAssignableFrom(clazz)) {
            return object(
                    FieldMetaData.getStructMetaDataMap((Class<? extends TBase>) clazz)
                            .keySet()
                            .stream()
                            .map(TFieldIdEnum::getFieldName)
                            .flatMap(fieldName -> ReflectionUtils.getFields(clazz, ReflectionUtils.withName(fieldName)).stream())
                            .map(field -> new SpecField(field.getName(), of(field.getGenericType())))
                            .toArray(SpecField[]::new)
            );
        }

        throw new IllegalArgumentException("Can't find corresponding spec type for - " + clazz);
    }
}
