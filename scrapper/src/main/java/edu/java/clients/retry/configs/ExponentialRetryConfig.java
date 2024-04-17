package edu.java.clients.retry.configs;

import edu.java.configuration.WebClientsConfig;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
@ConditionalOnProperty(prefix = "clients.connection", name = "strategy", havingValue = "exponential")
public class ExponentialRetryConfig {
    @Bean
    public Retry exponentialRetry(WebClientsConfig webClientsConfig) {
        return buildExponentialRetry(
            Duration.ofSeconds(webClientsConfig.connection().delay()),
            webClientsConfig.connection().attempts().intValue(),
            webClientsConfig.connection().codes()
        );
    }

    private Retry buildExponentialRetry(Duration initialDelay, int maxAttempts, List<Integer> retryCodes) {
        return Retry.backoff(maxAttempts, initialDelay)
            .filter(throwable -> throwable instanceof WebClientResponseException
                && retryCodes.contains(((WebClientResponseException) throwable).getStatusCode().value()))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }
}
