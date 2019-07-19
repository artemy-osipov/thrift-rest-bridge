package ru.osipov.thrift.bridge.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.platform.commons.util.ReflectionUtils
import ru.osipov.thrift.bridge.test.TestService
import ru.osipov.thrift.bridge.test.TestStruct
import ru.osipov.thrift.jackson.ThriftModule

import static ru.osipov.thrift.bridge.TestData.*

class ThriftConverterTest {

    ObjectMapper mapper = new ObjectMapper()

    ThriftConverter converter

    @Before
    void setup() {
        mapper.registerModule(new ThriftModule())
        converter = new ThriftConverter(mapper)
    }

    def method = ReflectionUtils.findMethod(TestService.Iface, OPERATION_NAME, String, TestStruct).get()

    @Test
    void "parseArgs should return array of args"() {
        def res = converter.parseArgs(method, restRequest())

        assert res == [THRIFT_SIMPLE_FIELD, thriftTestStruct()]
    }

    @Test
    void "parseArgs should return null element when json field dont exists"() {
        def res = converter.parseArgs(method, mapper.createObjectNode())

        assert res == [null, null]
    }
}