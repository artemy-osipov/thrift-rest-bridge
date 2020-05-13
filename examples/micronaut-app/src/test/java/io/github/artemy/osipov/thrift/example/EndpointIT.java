package io.github.artemy.osipov.thrift.example;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class EndpointIT {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void shouldAddServicesEndpoint() {
        String res = client.toBlocking().retrieve("/services");

        Assertions.assertNotEquals("", res);
    }
}