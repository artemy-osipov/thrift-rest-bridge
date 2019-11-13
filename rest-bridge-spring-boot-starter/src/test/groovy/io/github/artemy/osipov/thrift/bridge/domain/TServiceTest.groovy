package io.github.artemy.osipov.thrift.bridge.domain

import io.github.artemy.osipov.thrift.bridge.domain.exception.NotFoundException
import org.junit.Test
import io.github.artemy.osipov.thrift.bridge.test.TestService

import static groovy.test.GroovyAssert.shouldFail
import static io.github.artemy.osipov.thrift.bridge.TestData.*

class TServiceTest {

    def service = new TService(SERVICE_NAME, THRIFT_CLIENT_CLASS, [(OPERATION_NAME): new TOperation(OPERATION_NAME)])

    @Test
    void "build should construct TService"() {
        def res = TService.build(THRIFT_CLIENT_CLASS)

        assert res == service
    }

    @Test
    void "getOperation should return by name"() {
        def res = service.getOperation(OPERATION_NAME)

        assert res == operation()
    }

    @Test
    void "getOperation should fail when requested by unknown name"() {
        shouldFail(NotFoundException) {
            service.getOperation('unknown')
        }
    }

    @Test
    void "buildThriftClient should build client"() {
        def res = service.buildThriftClient(THRIFT_ENDPOINT)

        assert res instanceof TestService.Iface
    }
}