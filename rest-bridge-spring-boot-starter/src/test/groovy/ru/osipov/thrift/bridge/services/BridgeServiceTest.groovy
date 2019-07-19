package ru.osipov.thrift.bridge.services

import org.junit.Before
import org.junit.Test
import ru.osipov.thrift.bridge.domain.TService
import ru.osipov.thrift.bridge.test.TestService

import static org.mockito.Mockito.*
import static ru.osipov.thrift.bridge.TestData.*

class BridgeServiceTest {

    def thriftConverter = mock(ThriftConverter)
    def service = new BridgeService(thriftConverter)

    def thriftClient = mock(TestService.Client)
    def thriftService = mock(TService)
    def operation = operation().tap {
        service = thriftService
    }

    @Before
    void setup() {
        doReturn(thriftClient).when(thriftService).buildThriftClient(THRIFT_ENDPOINT)
        doReturn([THRIFT_SIMPLE_FIELD, thriftTestStruct()] as Object[]).when(thriftConverter).parseArgs(any(), eq(restRequest()))
    }

    @Test
    void "should proxy request to thrift"() {
        def resp = [thriftTestStruct()]
        doReturn(resp).when(thriftClient).testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct())

        def res = service.proxy(operation, THRIFT_ENDPOINT, restRequest())

        assert res == resp
    }

    @Test
    void "should proxy exception from thrift"() {
        def resp = thriftException()
        doThrow(resp).when(thriftClient).testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct())

        def res = service.proxy(operation, THRIFT_ENDPOINT, restRequest())

        assert res == resp
    }
}