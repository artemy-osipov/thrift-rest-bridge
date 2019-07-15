package ru.osipov.thrift.bridge.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.platform.commons.util.ReflectionUtils
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.junit4.SpringRunner
import ru.osipov.thrift.bridge.test.TestService
import ru.osipov.thrift.bridge.test.TestStruct

import static ru.osipov.thrift.bridge.TestData.*

@RunWith(SpringRunner)
@JsonTest
class ThriftConverterTest {

    @Autowired
    ObjectMapper mapper

    ThriftConverter converter

    @Before
    void setup() {
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

    @Test
    void "parseResponse should trim thrift specific fields"() {
        def res = converter.parseResponse(thriftResponse())

        assert res == restResponse()
    }

    @Test
    void "parseResponse should return empty object if null"() {
        def res = converter.parseResponse(null)

        assert res.isEmpty(null)
    }
}