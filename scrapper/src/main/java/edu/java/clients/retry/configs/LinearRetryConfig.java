package edu.java.clients.retry.configs;

import edu.java.configuration.WebClientsConfig;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Configuration
@ConditionalOnProperty(prefix = "clients.connection", name = "strategy", havingValue = "linear")
public class LinearRetryConfig {
    @Bean
    public Retry linearRetry(WebClientsConfig webClientsConfig) {
        return buildLinearRetry(
            Duration.ofSeconds(webClientsConfig.connection().delay()),
            webClientsConfig.connection().attempts().intValue(),
            webClientsConfig.connection().codes()
        );
    }

    private Retry buildLinearRetry(Duration initialDelay, int maxAttempts, List<Integer> retryCodes) {
        return Retry.from(fl -> fl
            .zipWith(Flux.range(1, maxAttempts), (signal, attempt) -> {
                if (signal.failure() instanceof WebClientResponseException
                    &&
                    retryCodes.contains(((WebClientResponseException) signal.failure()).getStatusCode().value())) {
                    return Duration.ofSeconds(attempt * initialDelay.toSeconds());
                }
                throw Exceptions.propagate(signal.failure());
            })
            .filter(Objects::nonNull)
            .flatMap(Mono::delay));
    }
}
