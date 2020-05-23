package io.github.artemy.osipov.thrift.bridge.spec

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository
import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.spec.IsEqualJson.json
import static io.github.artemy.osipov.thrift.bridge.spec.TestData.*
import static org.hamcrest.CoreMatchers.equalTo
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.argThat
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.doThrow

class BridgeSpec {

    private TServiceRepository serviceRepository
    private BridgeFacade bridgeFacade

    void init(int port, TServiceRepository mockServiceRepository, BridgeFacade mockBridgeFacade) {
        RestAssured.port = port
        serviceRepository = mockServiceRepository
        bridgeFacade = mockBridgeFacade

        initMocks()
    }

    void initMocks() {
        doReturn(service())
                .when(serviceRepository)
                .findById(SERVICE_ID)
    }

    @Test
    void "services endpoint should return list of services"() {
        def service = service()
        doReturn([service])
                .when(serviceRepository)
                .list()

        RestAssured.given()
                .get('/services')
                .then()
                .body(equalTo('[{"id":"io.github.artemy.osipov.thrift.bridge.test.TestService.Client","name":"TestService","operations":[{"name":"testOperation"}]}]'))
    }

    @Test
    void "services endpoint should return empty list when no services"() {
        doReturn([])
                .when(serviceRepository)
                .list()

        RestAssured.given()
                .get('/services')
                .then()
                .body(equalTo('[]'))
    }

    @Test
    void "services endpoint should return service requested by id"() {
        RestAssured.given()
                .get("/services/$SERVICE_ID")
                .then()
                .body('id', equalTo(SERVICE_ID))
                .body('name', equalTo(SERVICE_NAME))
    }

    @Test
    void "services endpoint should throw fault when service requested by unknown name"() {
        doThrow(new NotFoundException())
                .when(serviceRepository)
                .findById(any())

        RestAssured.given()
                .get('/services/unknown')
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    void "services-operations endpoint should proxy request to thrift"() {
        doReturn(thriftTestStruct())
                .when(bridgeFacade)
                .proxy(eq(operation()), eq(THRIFT_ENDPOINT), argThat(json(proxyRequestBody())))

        RestAssured.given()
                .body(proxyRequest())
                .contentType(ContentType.JSON)
                .post("/services/$SERVICE_ID/operations/$OPERATION_NAME")
                .then()
                .body('stringField', equalTo(thriftTestStruct().stringField))
    }

    @Test
    void "services-operations endpoint should interpret null proxy result as empty"() {
        doReturn(null)
                .when(bridgeFacade)
                .proxy(operation(), THRIFT_ENDPOINT, proxyRequestBody())

        RestAssured.given()
                .body(proxyRequest())
                .contentType(ContentType.JSON)
                .post("/services/$SERVICE_ID/operations/$OPERATION_NAME")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo(''))
    }

    @Test
    void "services-operations endpoint should fail when requested without endpoint"() {
        RestAssured.given()
                .body('{}')
                .contentType(ContentType.JSON)
                .post("/services/$SERVICE_ID/operations/$OPERATION_NAME")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
    }

    @Test
    void "services-operations-template endpoint should build template"() {
        int depth = 10
        doReturn(templateSpec())
                .when(bridgeFacade)
                .template(operation(), depth)

        RestAssured.given()
                .queryParam('depth', depth)
                .get("/services/$SERVICE_ID/operations/$OPERATION_NAME/template")
                .then()
                .body(equalTo(templateSpec()))
    }
}
