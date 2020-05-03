package io.github.artemy.osipov.thrift.bridge.core

import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.TestData.*
import static io.github.artemy.osipov.thrift.bridge.utils.JsonUtils.toJson

class ArgumentParserTest {
    def argumentParser = new ArgumentParser()
    def operation = operation()

    @Test
    void "should parse arguments from json"() {
        def res = argumentParser.parse(operation.args, toJson(proxyRequestBody()))

        assert res[0] == THRIFT_SIMPLE_FIELD
        assert res[1].equals(thriftTestStruct())
    }

    @Test
    void "should compute null element when json field dont exists"() {
        def res = argumentParser.parse(operation.args, "{}")

        assert res == new Object[]{null, null}
    }
}