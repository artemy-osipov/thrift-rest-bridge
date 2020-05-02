package io.github.artemy.osipov.thrift.bridge.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.util.ReflectionUtils
import io.github.artemy.osipov.thrift.bridge.test.TestService
import io.github.artemy.osipov.thrift.bridge.test.TestStruct
import io.github.artemy.osipov.thrift.jackson.ThriftModule

import static io.github.artemy.osipov.thrift.bridge.TestData.*

class ThriftConverterTest {

    ObjectMapper mapper = new ObjectMapper()

    ThriftConverter converter

    @BeforeEach
    void setup() {
        mapper.registerModule(new ThriftModule())
        converter = new ThriftConverter(mapper)
    }

    def method = ReflectionUtils.findMethod(TestService.Iface, OPERATION_NAME, String, TestStruct).get()

    @Test
    void "parseArgs should return array of args"() {
        def res = converter.parseArgs(method, restRequest())

        assert res[0] == THRIFT_SIMPLE_FIELD
        assert res[1].equals(thriftTestStruct())
    }

    @Test
    void "parseArgs should return null element when json field dont exists"() {
        def res = converter.parseArgs(method, mapper.createObjectNode())

        assert res == new Object[]{null, null}
    }
}