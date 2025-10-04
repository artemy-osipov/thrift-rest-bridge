package io.github.artemy.osipov.thrift.bridge.spring.controllers

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository
import io.github.artemy.osipov.thrift.bridge.spec.BridgeSpec
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(
        properties = "spring.main.allow-bean-definition-overriding=true",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BridgeControllerIT extends BridgeSpec {

    @LocalServerPort
    int port

    @MockitoBean
    TServiceRepository serviceRepository

    @MockitoBean
    BridgeFacade bridgeFacade

    @BeforeEach
    void setup() {
        init(
                port,
                serviceRepository,
                bridgeFacade
        )
    }
}