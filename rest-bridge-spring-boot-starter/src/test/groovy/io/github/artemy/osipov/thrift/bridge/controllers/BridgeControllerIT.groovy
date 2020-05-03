package io.github.artemy.osipov.thrift.bridge.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.artemy.osipov.thrift.bridge.config.BridgeAutoConfiguration
import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import io.github.artemy.osipov.thrift.bridge.core.BridgeService
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository
import io.github.artemy.osipov.thrift.bridge.utils.JsonMatcher
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import static io.github.artemy.osipov.thrift.bridge.TestData.*
import static org.hamcrest.Matchers.is
import static org.mockito.ArgumentMatchers.*
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.doThrow
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ContextConfiguration(classes = [BridgeAutoConfiguration, CustomizationConfiguration])
@WebMvcTest(BridgeController)
@AutoConfigureRestDocs
class BridgeControllerIT {

    @TestConfiguration
    static class CustomizationConfiguration implements RestDocsMockMvcConfigurationCustomizer {

        @Override
        void customize(MockMvcRestDocumentationConfigurer configurer) {
            configurer.operationPreprocessors()
                    .withRequestDefaults(Preprocessors.prettyPrint())
                    .withResponseDefaults(Preprocessors.prettyPrint())
        }

    }

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper mapper

    @MockBean
    private TServiceRepository thriftRepository

    @MockBean
    private BridgeService bridgeService

    @Test
    void "services endpoint should return list of services"() {
        def service = service()
        doReturn([service]).when(thriftRepository).list()

        def req = get("/services")
                .contentType(APPLICATION_JSON)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].name', is(service.name)))
                .andExpect(jsonPath('$[0].operations[*].name', is(service.operations.values().name)))
                .andDo(document("list-services",
                        responseFields(
                                fieldWithPath('[].name').description("Service name"),
                                subsectionWithPath('[].operations').description("Available operations")
                        )
                ))
    }

    @Test
    void "services endpoint should return empty list when no services"() {
        doReturn([]).when(thriftRepository).list()

        def req = get("/services")
                .contentType(APPLICATION_JSON)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().string('[]'))
    }

    @Test
    void "services endpoint should return service requested by name"() {
        doReturn(service()).when(thriftRepository).findByName(SERVICE_NAME)

        def req = get("/services/{service}", SERVICE_NAME)
                .contentType(APPLICATION_JSON)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name', is(SERVICE_NAME)))
                .andDo(document("show-service",
                        pathParameters(
                                parameterWithName("service").description("Service name")
                        ),
                        responseFields(
                                fieldWithPath('name').description("Service name"),
                                subsectionWithPath('operations').description("Available operations")
                        )
                ))
    }

    @Test
    void "services endpoint should throw fault when service requested by unknown name"() {
        doThrow(new NotFoundException()).when(thriftRepository).findByName(any())

        def req = get("/services/{service}", 'unknown')
                .contentType(APPLICATION_JSON)

        mockMvc.perform(req)
                .andExpect(status().isNotFound())
    }

    @Test
    void "services-operations endpoint should proxy request to thrift"() {
        def restRequest = restRequest()
        doReturn(service()).when(thriftRepository).findByName(SERVICE_NAME)
        doReturn(thriftTestStruct())
                .when(bridgeService)
                .proxy(eq(operation()), eq(THRIFT_ENDPOINT), argThat(new JsonMatcher(restRequest)))

        def req = post("/services/{service}/operations/{operation}", SERVICE_NAME, OPERATION_NAME)
                .header('Thrift-Endpoint', THRIFT_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(restRequest))

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(jsonTestStruct())))
                .andDo(document("run-operation",
                        pathParameters(
                                parameterWithName("service").description("Service name"),
                                parameterWithName("operation").description("Operation name")
                        ),
                        requestHeaders(
                                headerWithName("Thrift-Endpoint").description("Thrift endpoint where the request will proxy"))
                ))
    }

    @Test
    void "services-operations endpoint should interpret null proxy result as empty"() {
        def restRequest = restRequest()
        doReturn(service()).when(thriftRepository).findByName(SERVICE_NAME)
        doReturn(null).when(bridgeService).proxy(operation(), THRIFT_ENDPOINT, restRequest)

        def req = post("/services/{service}/operations/{operation}", SERVICE_NAME, OPERATION_NAME)
                .header('Thrift-Endpoint', THRIFT_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(restRequest))

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().string(""))
    }

    @Test
    void "services-operations endpoint should fail when requested without endpoint"() {
        def req = post("/services/{service}/operations/{operation}", SERVICE_NAME, OPERATION_NAME)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(restRequest()))

        mockMvc.perform(req)
                .andExpect(status().isBadRequest())
    }
}