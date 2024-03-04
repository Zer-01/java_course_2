package edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clients")
public record WebClientsConfig(
    @NotNull
    Urls urls,
    @NotNull
    Connection connection
) {
    public record Urls(String scrapper) {
    }

    public record Connection(Long attempts, Long timeout) {
    }
}
