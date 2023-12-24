package com.recipify.recipify.integration.services.integrations.hunter;

import com.recipify.recipify.api.exception.IntegrationException;
import com.recipify.recipify.services.integrations.hunter.HunterConfiguration;
import com.recipify.recipify.services.integrations.hunter.HunterWebClient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HunterWebClientTest {

    private static MockWebServer mockWebServer;
    private static HunterWebClient hunterWebClient;

    @BeforeAll
    static void startMockServer() throws IOException, NoSuchFieldException, IllegalAccessException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        hunterWebClient = createHunterWebClient(webClient);
    }

    @AfterAll
    static void shutdownMockServer() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void verifyEmail_valid() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                            "data": {
                                "status": "valid"
                            }
                        }
                        """));

        // WHEN
        boolean result = hunterWebClient.verifyEmail("test@test.com");

        // THEN
        assertTrue(result);

    }

    @Test
    public void verifyEmail_invalid() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                            "data": {
                                "status": "invalid"
                            }
                        }
                        """));

        // WHEN
        boolean result = hunterWebClient.verifyEmail("test@test.com");

        // THEN
        assertFalse(result);

    }

    @Test
    public void verifyEmail_unauthorized() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        // WHEN THEN
        assertThrows(IntegrationException.class, () -> hunterWebClient.verifyEmail("test@test.com"));
    }

    @Test
    public void verifyEmail_badRequest() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        // WHEN THEN
        assertThrows(IntegrationException.class, () -> hunterWebClient.verifyEmail("test@test.com"));
    }

    private static HunterWebClient createHunterWebClient(WebClient webClient) throws NoSuchFieldException, IllegalAccessException {
        HunterWebClient hunterWebClient = new HunterWebClient(new HunterConfiguration());
        Field webClientField = HunterWebClient.class.getDeclaredField("webClient");
        webClientField.setAccessible(true);
        webClientField.set(hunterWebClient, webClient);
        return hunterWebClient;
    }

}
