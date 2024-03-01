package edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "urls")
public record BaseUrlsConfig(
    String scrapper
) {
}
