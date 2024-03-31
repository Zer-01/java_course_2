package edu.java.bot.configuration;

import edu.java.bot.api.clients.retry.RetryStrategy;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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

    public record Connection(Long attempts, Long delay, Long timeout, RetryStrategy strategy, List<Integer> codes) {
    }
}
