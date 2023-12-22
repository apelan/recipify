package com.recipify.recipify.services.integrations.hunter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "hunter-api")
@Getter
@Setter
public class HunterConfiguration {

    private String url;
    private String apiKey;

}
