package com.recipify.recipify.services.integrations.clearbit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "clearbit-api")
@Getter
@Setter
public class ClearbitConfiguration {

    private String url;
    private String apiKey;

}
