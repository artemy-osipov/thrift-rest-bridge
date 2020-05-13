package io.github.artemy.osipov.thrift.bridge.micronaut.controllers

import groovy.json.JsonSlurper
import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository
import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto.ProxyRequest
import io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto.Service
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import javax.inject.Inject

import static io.github.artemy.osipov.thrift.bridge.micronaut.TestData.*
import static org.junit.Assert.assertThrows
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.doThrow

@MicronautTest
class BridgeControllerIT {

    @Inject
    @Client("/")
    RxHttpClient client

    @Inject
    TServiceRepository thriftRepository

    @Inject
    BridgeFacade bridgeService

    @BeforeEach
    void init() {
        doReturn(service())
                .when(thriftRepository)
                .findById(SERVICE_ID)
    }

    @Test
    void "services endpoint should return list of services"() {
        def service = service()
        doReturn([service])
                .when(thriftRepository)
                .list()

        def res = client.toBlocking().retrieve('/services')

        assert res == '[{"id":"io.github.artemy.osipov.thrift.bridge.test.TestService.Client","name":"TestService","operations":[{"name":"testOperation"}]}]'
    }

    @Test
    void "services endpoint should return empty list when no services"() {
        doReturn([])
                .when(thriftRepository)
                .list()

        def res = client.toBlocking().retrieve('/services', List<?>)

        assert res == []
    }

    @Test
    void "services endpoint should return service requested by id"() {
        def res = client.toBlocking().retrieve("/services/$SERVICE_ID", Service)

        assert res.id == SERVICE_ID
        assert res.name == SERVICE_NAME
    }

    @Test
    void "services endpoint should throw fault when service requested by unknown name"() {
        doThrow(new NotFoundException())
                .when(thriftRepository)
                .findById(any())

        def ex = assertThrows(HttpClientResponseException) {
            client.toBlocking().exchange('/services/unknown')
        }

        assert ex.status == HttpStatus.NOT_FOUND
    }

    @Test
    void "services-operations endpoint should proxy request to thrift"() {
        doReturn(thriftTestStruct())
                .when(bridgeService)
                .proxy(operation(), THRIFT_ENDPOINT, proxyRequestBody())

        def res = client.toBlocking().retrieve(
                HttpRequest.POST("/services/$SERVICE_ID/operations/$OPERATION_NAME", proxyRequest())
        )

        assert new JsonSlurper().parseText(res)['stringField'] == thriftTestStruct().stringField
    }

    @Test
    void "services-operations endpoint should interpret null proxy result as empty"() {
        doReturn(null)
                .when(bridgeService)
                .proxy(operation(), THRIFT_ENDPOINT, proxyRequestBody())

        def ex = assertThrows(HttpClientResponseException) {
            client.toBlocking().retrieve(
                    HttpRequest.POST("/services/$SERVICE_ID/operations/$OPERATION_NAME", proxyRequest())
            )
        }

        assert ex.status == HttpStatus.OK
        assert ex.response.body() == null
    }

    @Test
    void "services-operations endpoint should fail when requested without endpoint"() {
        def ex = assertThrows(HttpClientResponseException) {
            client.toBlocking().retrieve(
                    HttpRequest.POST("/services/$SERVICE_ID/operations/$OPERATION_NAME", new ProxyRequest())
            )
        }

        assert ex.status == HttpStatus.BAD_REQUEST
    }

    @Test
    void "services-operations-template endpoint should build template"() {
        int depth = 10
        doReturn(templateSpec())
                .when(bridgeService)
                .template(operation(), depth)

        def res = client.toBlocking().retrieve("/services/$SERVICE_ID/operations/$OPERATION_NAME/template?depth=$depth")

        assert res == templateSpec()
    }
}