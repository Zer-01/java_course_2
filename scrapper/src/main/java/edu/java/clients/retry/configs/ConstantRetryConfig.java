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
@ConditionalOnProperty(prefix = "clients.connection", name = "strategy", havingValue = "constant")
public class ConstantRetryConfig {
    @Bean
    public Retry constantRetry(WebClientsConfig webClientsConfig) {
        return buildConstantRetry(
            Duration.ofSeconds(webClientsConfig.connection().delay()),
            webClientsConfig.connection().attempts().intValue(),
            webClientsConfig.connection().codes()
        );
    }

    private Retry buildConstantRetry(Duration initialDelay, int maxAttempts, List<Integer> retryCodes) {
        return Retry.fixedDelay(maxAttempts, initialDelay)
            .filter(throwable -> throwable instanceof WebClientResponseException
                && retryCodes.contains(((WebClientResponseException) throwable).getStatusCode().value()))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }
}
