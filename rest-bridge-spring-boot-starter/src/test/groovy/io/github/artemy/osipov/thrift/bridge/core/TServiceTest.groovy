package io.github.artemy.osipov.thrift.bridge.core

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import io.github.artemy.osipov.thrift.bridge.test.TestService
import org.junit.jupiter.api.Test

import static groovy.test.GroovyAssert.shouldFail
import static io.github.artemy.osipov.thrift.bridge.TestData.*

class TServiceTest {

    def service = new TService(THRIFT_CLIENT_CLASS)

    @Test
    void "should construct TService"() {
        assert service.name == SERVICE_NAME
        assert service.thriftServiceClass == THRIFT_CLIENT_CLASS
        assert service.operations == [(OPERATION_NAME): operation()]
    }

    @Test
    void "getOperation should return by name"() {
        def res = service.operation(OPERATION_NAME)

        assert res == operation()
    }

    @Test
    void "getOperation should fail when requested by unknown name"() {
        shouldFail(NotFoundException) {
            service.operation('unknown')
        }
    }

    @Test
    void "buildThriftClient should build client"() {
        def res = service.buildThriftClient(THRIFT_ENDPOINT)

        assert res instanceof TestService.Iface
    }
}