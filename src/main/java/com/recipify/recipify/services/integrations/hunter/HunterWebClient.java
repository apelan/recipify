package com.recipify.recipify.services.integrations.hunter;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HunterWebClient {

    private final HunterConfiguration configuration;

    private WebClient webClient;

    @PostConstruct
    public void setupWebClient() {
        webClient = WebClient.builder()
                .baseUrl(configuration.getUrl())
                .build();
    }

    /**
     * Method used to call Hunter API in order to verify email address.
     *
     * @param email address to verify
     * @return true if valid, false otherwise
     */
    public boolean verifyEmail(String email) {
        log.info("Calling Hunter API to validate email address: {}", email);
        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("email", email)
                        .queryParam("api_key", configuration.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response != null && response.has("data")
                && response.get("data").has("status")
                && response.get("data").get("status").asText().equals("valid");
    }

}
