package io.github.artemy.osipov.thrift.bridge.core

import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.core.TestData.*

class TArgumentsTest {
    def arguments = operation().getArguments()

    @Test
    void "should parse arguments from json"() {
        def res = arguments.args(proxyRequestBody())

        assert res[0] == THRIFT_SIMPLE_FIELD
        assert res[1] == thriftTestStruct()
        assert res[2] == [thriftTestInnerStruct()]
        assert res[3] == [thriftTestInnerStruct()].toSet()
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