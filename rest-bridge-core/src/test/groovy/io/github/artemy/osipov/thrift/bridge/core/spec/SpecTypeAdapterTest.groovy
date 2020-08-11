package io.github.artemy.osipov.thrift.bridge.core.spec

import com.fasterxml.jackson.core.type.TypeReference
import io.github.artemy.osipov.thrift.bridge.test.TestEnum
import io.github.artemy.osipov.thrift.bridge.test.TestRecursiveStruct
import io.github.artemy.osipov.thrift.bridge.test.TestSimpleStruct
import io.github.artemy.osipov.thrift.bridge.test.TestUnion
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

import java.nio.ByteBuffer
import java.util.stream.Stream

import static io.github.artemy.osipov.thrift.bridge.core.TestData.*

class SpecTypeAdapterTest {

    def adapter = new SpecTypeAdapter()

    @ParameterizedTest
    @MethodSource('buildSpecFromPrimitivesArgs')
    void "should build spec from primitives"(Class<?> primitiveClazz, SpecType specType) {
        def spec = adapter.from(primitiveClazz)

        assert spec == specType
    }

    static Stream<Arguments> buildSpecFromPrimitivesArgs() {
        return Stream.of(
                Arguments.of(boolean.class, SpecType.primitive(DataType.BOOLEAN)),
                Arguments.of(Boolean.class, SpecType.primitive(DataType.BOOLEAN)),
                Arguments.of(byte.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(Byte.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(short.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(Short.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(int.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(Integer.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(long.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(Long.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(float.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(Float.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(double.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(Double.class, SpecType.primitive(DataType.NUMBER)),
                Arguments.of(String.class, SpecType.primitive(DataType.STRING)),
                Arguments.of(Enum.class, SpecType.primitive(DataType.ENUM)),
                Arguments.of(byte[].class, SpecType.primitive(DataType.STRING)),
                Arguments.of(ByteBuffer.class, SpecType.primitive(DataType.STRING))
        )
    }

    @Test
    void "should build spec from enum"() {
        def spec = adapter.from(TestEnum)

        assert spec == SpecType.primitive(DataType.ENUM)
    }

    @Test
    void "should build spec from struct"() {
        def spec = adapter.from(TestSimpleStruct)

        assert spec == simpleStructSpecType()
    }

    @Test
    void "should build spec from union"() {
        def spec = adapter.from(TestUnion)

        assert spec == SpecType.object(
                new SpecField("enum1", SpecType.primitive(DataType.ENUM)),
                new SpecField("enum2", SpecType.primitive(DataType.ENUM))
        )
    }

    @Test
    void "should build spec from collection of primitive"() {
        def spec = adapter.from(new TypeReference<Collection<Integer>>(){}.getType())

        assert spec == SpecType.array(SpecType.primitive(DataType.NUMBER))
    }

    @Test
    void "should build spec from collection of struct"() {
        def spec = adapter.from(new TypeReference<Collection<TestSimpleStruct>>(){}.getType())

        assert spec == SpecType.array(simpleStructSpecType())
    }

    @Test
    void "should build spec from recursive struct"() {
        def spec = adapter.from(TestRecursiveStruct)

        assert spec.type == DataType.OBJECT
        assert spec.containerType == null
        assert spec.nested == [
                new SpecField('f1', SpecType.primitive(DataType.STRING)),
                new SpecField('recursive', spec)
        ]
    }
}
