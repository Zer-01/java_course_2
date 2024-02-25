package edu.java.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "urls")
public record BaseUrlsConfig(
    String github,
    String stackoverflow
) {
}
