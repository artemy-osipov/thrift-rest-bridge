package io.github.artemy.osipov.thrift.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import io.github.artemy.osipov.thrift.bridge.domain.TService;
import io.github.artemy.osipov.thrift.bridge.services.TServiceRepository;
import io.github.artemy.osipov.thrift.bridge.test.TestService;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EndpointIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TServiceRepository thriftRepository;

    @Test
    public void shouldAddServicesEndpoint() throws Exception {
        TService service = TService.build(TestService.Client.class);
        doReturn(Collections.singletonList(service)).when(thriftRepository).list();

        mockMvc.perform(get("/services")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(service.getName())));
    }
}