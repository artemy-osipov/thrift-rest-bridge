package io.github.artemy.osipov.thrift.bridge.core

import org.junit.jupiter.api.Test

import java.lang.reflect.Parameter

import static io.github.artemy.osipov.thrift.bridge.utils.JsonUtils.toJson
import static org.mockito.Mockito.*
import static io.github.artemy.osipov.thrift.bridge.TestData.*

class BridgeFacadeTest {

    def service = new BridgeFacade()
    def operation = mock(TService.TOperation)

    @Test
    void "should delegate proxy to operation"() {
        def arguments = new TArguments(new Parameter[0])
        doReturn(arguments)
                .when(operation)
                .getArguments()
        def resp = thriftTestStruct()
        doReturn(resp)
                .when(operation)
                .proxy(eq(THRIFT_ENDPOINT), any())

        def res = service.proxy(operation, THRIFT_ENDPOINT, toJson(proxyRequestBody()))

        assert res == resp
    }
}
