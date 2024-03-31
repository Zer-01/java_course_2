package edu.java.configuration;

import edu.java.clients.retry.RetryStrategy;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "clients")
public record WebClientsConfig(
    @NotNull
    Urls urls,
    @NotNull
    Connection connection
) {
    public record Urls(String github, String stackoverflow, String bot) {
    }

    public record Connection(Long attempts, Long delay, Long timeout, RetryStrategy strategy, List<Integer> codes) {
    }
}
