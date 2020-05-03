package io.github.artemy.osipov.thrift.bridge.core

import org.junit.jupiter.api.Test

import java.lang.reflect.Parameter

import static io.github.artemy.osipov.thrift.bridge.utils.JsonUtils.toJson
import static org.mockito.Mockito.*
import static io.github.artemy.osipov.thrift.bridge.TestData.*

class BridgeServiceTest {

    def service = new BridgeService(new ArgumentParser())
    def operation = mock(TService.TOperation)

    @Test
    void "should delegate proxy to operation"() {
        Parameter[] args = []
        doReturn(args)
                .when(operation)
                .getArgs()
        def resp = thriftTestStruct()
        doReturn(resp)
                .when(operation)
                .proxy(THRIFT_ENDPOINT, args)

        def res = service.proxy(operation, THRIFT_ENDPOINT, toJson(proxyRequestBody()))

        assert res == resp
    }
}
