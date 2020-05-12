package io.github.artemy.osipov.thrift.bridge.spring.controllers

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository
import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import io.github.artemy.osipov.thrift.bridge.spring.config.BridgeAutoConfiguration
import io.github.artemy.osipov.thrift.bridge.spring.controllers.dto.ProxyRequestDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.RequestPostProcessor

import java.nio.charset.StandardCharsets

import static io.github.artemy.osipov.thrift.bridge.spring.TestData.*
import static io.github.artemy.osipov.thrift.bridge.spring.utils.JsonUtils.asNormalizedJson
import static io.github.artemy.osipov.thrift.bridge.spring.utils.JsonUtils.toJson
import static org.hamcrest.Matchers.is
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.doThrow
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ContextConfiguration(classes = BridgeAutoConfiguration)
@WebMvcTest(BridgeController)
class BridgeControllerIT {

    @Autowired
    MockMvc mockMvc

    @MockBean
    TServiceRepository thriftRepository

    @MockBean
    BridgeFacade bridgeService

    def jsonUtf8Processor = new RequestPostProcessor() {

        @Override
        MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            request.setContentType(MediaType.APPLICATION_JSON_VALUE)
            request.setCharacterEncoding(StandardCharsets.UTF_8.name())

            return request
        }
    }

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

        def req = get("/services")
                .with(jsonUtf8Processor)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].id', is(service.id)))
                .andExpect(jsonPath('$[0].name', is(service.name)))
                .andExpect(jsonPath('$[0].operations[*].name', is(service.operations.values().name)))
    }

    @Test
    void "services endpoint should return empty list when no services"() {
        doReturn([])
                .when(thriftRepository)
                .list()

        def req = get("/services")
                .with(jsonUtf8Processor)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().string('[]'))
    }

    @Test
    void "services endpoint should return service requested by id"() {
        def req = get("/services/{service}", SERVICE_ID)
                .with(jsonUtf8Processor)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id', is(SERVICE_ID)))
                .andExpect(jsonPath('$.name', is(SERVICE_NAME)))
    }

    @Test
    void "services endpoint should throw fault when service requested by unknown name"() {
        doThrow(new NotFoundException())
                .when(thriftRepository)
                .findById(any())

        def req = get("/services/{service}", 'unknown')
                .with(jsonUtf8Processor)

        mockMvc.perform(req)
                .andExpect(status().isNotFound())
    }

    @Test
    void "services-operations endpoint should proxy request to thrift"() {
        doReturn(thriftTestStruct())
                .when(bridgeService)
                .proxy(operation(), THRIFT_ENDPOINT, asNormalizedJson(proxyRequestBody()))

        def req = post("/services/{service}/operations/{operation}", SERVICE_ID, OPERATION_NAME)
                .with(jsonUtf8Processor)
                .content(proxyRequest())

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(thriftTestStruct())))
    }

    @Test
    void "services-operations endpoint should interpret null proxy result as empty"() {
        doReturn(null)
                .when(bridgeService)
                .proxy(operation(), THRIFT_ENDPOINT, asNormalizedJson(proxyRequestBody()))

        def req = post("/services/{service}/operations/{operation}", SERVICE_ID, OPERATION_NAME)
                .with(jsonUtf8Processor)
                .content(proxyRequest())

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().string(""))
    }

    @Test
    void "services-operations endpoint should fail when requested without endpoint"() {
        def req = post("/services/{service}/operations/{operation}", SERVICE_ID, OPERATION_NAME)
                .with(jsonUtf8Processor)
                .content(toJson(new ProxyRequestDTO()))

        mockMvc.perform(req)
                .andExpect(status().isBadRequest())
    }

    @Test
    void "services-operations-template endpoint should build template"() {
        int depth = 10
        doReturn(templateSpec())
                .when(bridgeService)
                .template(operation(), depth)

        def req = get("/services/{service}/operations/{operation}/template", SERVICE_ID, OPERATION_NAME)
                .with(jsonUtf8Processor)
                .param("depth", "$depth")

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().json(templateSpec()))
    }
}