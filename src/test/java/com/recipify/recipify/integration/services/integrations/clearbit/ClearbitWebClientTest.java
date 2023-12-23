package com.recipify.recipify.integration.services.integrations.clearbit;

import com.recipify.recipify.services.integrations.clearbit.ClearbitConfiguration;
import com.recipify.recipify.services.integrations.clearbit.ClearbitUser;
import com.recipify.recipify.services.integrations.clearbit.ClearbitWebClient;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearbitWebClientTest {

    private static MockWebServer mockWebServer;
    private static ClearbitWebClient clearbitWebClient;

    @BeforeAll
    static void startMockServer() throws IOException, NoSuchFieldException, IllegalAccessException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        clearbitWebClient = createClearbitWebClient(webClient);
    }

    @AfterAll
    static void shutdownMockServer() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getUserData_allUserInfoPresent() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                            "name": {
                                "givenName": "Lorem",
                                "familyName": "Ipsum"
                            },
                            "geo": {
                                "city": "Paris",
                                "country": "France"
                            }
                        }
                        """));

        // WHEN
        ClearbitUser result = clearbitWebClient.getUserData("test@test.com");

        // THEN
        assertNotNull(result);
        assertEquals("Lorem", result.givenName());
        assertEquals("Ipsum", result.familyName());
        assertEquals("Paris", result.city());
        assertEquals("France", result.country());

    }

    @Test
    public void getUserData_missingGeolocationInfo() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                            "name": {
                                "givenName": "Lorem",
                                "familyName": "Ipsum"
                            }
                        }
                        """));

        // WHEN
        ClearbitUser result = clearbitWebClient.getUserData("test@test.com");

        // THEN
        assertNotNull(result);
        assertEquals("Lorem", result.givenName());
        assertEquals("Ipsum", result.familyName());
        assertNull(result.city());
        assertNull(result.country());

    }

    private static ClearbitWebClient createClearbitWebClient(WebClient webClient) throws NoSuchFieldException, IllegalAccessException {
        ClearbitWebClient clearbitWebClient = new ClearbitWebClient(new ClearbitConfiguration());
        Field webClientField = ClearbitWebClient.class.getDeclaredField("webClient");
        webClientField.setAccessible(true);
        webClientField.set(clearbitWebClient, webClient);
        return clearbitWebClient;
    }

}
