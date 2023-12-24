package com.recipify.recipify.services.integrations.clearbit;

import com.fasterxml.jackson.databind.JsonNode;
import com.recipify.recipify.api.exception.IntegrationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClearbitWebClient {

    private final ClearbitConfiguration configuration;

    private WebClient webClient;

    @PostConstruct
    public void setupWebClient() {
        webClient = WebClient.builder()
                .baseUrl(configuration.getUrl())
                .build();
    }

    /**
     * Used to call Clearbit API to fetch user information based on email address.
     *
     * @param email address of user
     * @return {@link ClearbitUser}
     */
    public ClearbitUser getUserData(String email) {
        log.info("Calling Clearbit API to fetch user data, email: {}", email);

        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("email", email)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + configuration.getApiKey())
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.value() == 400,
                        clientResponse -> {
                            throw new IntegrationException("Issue during Clearbit call", HttpStatus.BAD_REQUEST);
                        }
                ).onStatus(
                        httpStatus -> httpStatus.value() == 401,
                        clientResponse -> {
                            throw new IntegrationException("Authorization issue, check Clearbit api key", HttpStatus.BAD_REQUEST);
                        }
                )
                .bodyToMono(JsonNode.class)
                .block();

        return mapResponseToUser(response);
    }

    /**
     * Used to fetch data from JsonNode response and create ClearbitUser.
     *
     * @param response {@link JsonNode}
     * @return {@link ClearbitUser}
     */
    private ClearbitUser mapResponseToUser(JsonNode response) {
        String givenName = null;
        String familyName = null;
        String city = null;
        String country = null;

        if (response != null && response.has("name")) {
            if (response.get("name").has("givenName")) {
                givenName = response.get("name").get("givenName").asText();
            }

            if (response.get("name").has("familyName")) {
                familyName = response.get("name").get("familyName").asText();
            }
        }

        if (response != null && response.has("geo")) {
            if (response.get("geo").has("city")) {
                city = response.get("geo").get("city").asText();
            }

            if (response.get("geo").has("country")) {
                country = response.get("geo").get("country").asText();
            }
        }

        return new ClearbitUser(givenName, familyName, city, country);
    }

}
