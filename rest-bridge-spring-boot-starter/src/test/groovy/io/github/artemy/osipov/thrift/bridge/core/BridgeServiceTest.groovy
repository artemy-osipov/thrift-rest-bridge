package io.github.artemy.osipov.thrift.bridge.core

import io.github.artemy.osipov.thrift.bridge.test.TestService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.mockito.Mockito.*
import static io.github.artemy.osipov.thrift.bridge.TestData.*

class BridgeServiceTest {

    def thriftConverter = mock(ThriftConverter)
    def service = new BridgeService(thriftConverter)

    def thriftClient = mock(TestService.Client)
    def thriftClientFactory = mock(ThriftClientFactory)
    def operation = new TService(THRIFT_CLIENT_CLASS, thriftClientFactory).operation(OPERATION_NAME)

    @BeforeEach
    void setup() {
        doReturn(thriftClient)
                .when(thriftClientFactory)
                .build(THRIFT_CLIENT_CLASS, THRIFT_ENDPOINT)
        doReturn([THRIFT_SIMPLE_FIELD, thriftTestStruct()] as Object[])
                .when(thriftConverter)
                .parseArgs(any(), eq(restRequest()))
    }

    @Test
    void "should proxy request to thrift"() {
        def resp = [thriftTestStruct()]
        doReturn(resp)
                .when(thriftClient)
                .testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct())

        def res = service.proxy(operation, THRIFT_ENDPOINT, restRequest())

        assert res == resp
    }

    @Test
    void "should proxy exception from thrift"() {
        def resp = thriftException()
        doThrow(resp)
                .when(thriftClient)
                .testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct())

        def res = service.proxy(operation, THRIFT_ENDPOINT, restRequest())

        assert res == resp
    }
}
