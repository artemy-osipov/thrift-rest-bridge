package io.github.artemy.osipov.thrift.bridge.core

import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.TestData.*
import static io.github.artemy.osipov.thrift.bridge.utils.JsonUtils.toJson

class TArgumentsTest {
    def arguments = operation().getArguments()

    @Test
    void "should parse arguments from json"() {
        def res = arguments.args(toJson(proxyRequestBody()))

        assert res[0] == THRIFT_SIMPLE_FIELD
        assert res[1].equals(thriftTestStruct())
        assert res[2].equals([thriftTestInnerStruct()])
        assert res[3].equals([thriftTestInnerStruct()].toSet())
    }

    @Test
    void "should compute null element when json field dont exists"() {
        def res = arguments.args("{}")

        assert res == new Object[]{null, null, null, null}
    }

    @Test
    void "should compute specType from parameters"() {
        def res = arguments.specType()

        assert res == proxyRequestSpecType()
    }
}