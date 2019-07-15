package ru.osipov.thrift.bridge.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import ru.osipov.thrift.bridge.domain.exception.NotFoundException
import ru.osipov.thrift.bridge.services.BridgeService
import ru.osipov.thrift.bridge.services.TServiceRepository

import static org.hamcrest.Matchers.is
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.doThrow
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static ru.osipov.thrift.bridge.TestData.*

@RunWith(SpringRunner)
@WebMvcTest(BridgeController)
class BridgeControllerIT {

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
                .contentType(APPLICATION_JSON_UTF8)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].name', is(service.name)))
                .andExpect(jsonPath('$[0].operations', is(service.operations.values().name)))
    }

    @Test
    void "services endpoint should return empty list when no services"() {
        doReturn([]).when(thriftRepository).list()

        def req = get("/services")
                .contentType(APPLICATION_JSON_UTF8)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().string('[]'))
    }

    @Test
    void "services endpoint should return service requested by name"() {
        doReturn(service()).when(thriftRepository).findByName(SERVICE_NAME)

        def req = get("/services/{service}", SERVICE_NAME)
                .contentType(APPLICATION_JSON_UTF8)

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name', is(SERVICE_NAME)))
    }

    @Test
    void "services endpoint should throw fault when service requested by unknown name"() {
        doThrow(new NotFoundException()).when(thriftRepository).findByName(any())

        def req = get("/services/{service}", 'unknown')
                .contentType(APPLICATION_JSON_UTF8)

        mockMvc.perform(req)
                .andExpect(status().isNotFound())
    }

    @Test
    void "services-operations endpoint should proxy request to thrift"() {
        def restRequest = restRequest()
        def restResponse = restResponse()
        doReturn(service()).when(thriftRepository).findByName(SERVICE_NAME)
        doReturn(restResponse).when(bridgeService).proxy(operation(), THRIFT_ENDPOINT, restRequest)

        def req = post("/services/{service}/operations/{operation}", SERVICE_NAME, OPERATION_NAME)
                .header('Thrift-Endpoint', THRIFT_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(restRequest))

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(restResponse)))
    }

    @Test
    void "services-operations endpoint should fail when requested without endpoint"() {
        def req = post("/services/{service}/operations/{operation}", SERVICE_NAME, OPERATION_NAME)
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(restRequest()))

        mockMvc.perform(req)
                .andExpect(status().isBadRequest())
    }
}