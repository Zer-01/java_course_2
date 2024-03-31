package edu.java.bot.api.clients.retry;

import edu.java.bot.configuration.WebClientsConfig;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class RetryConfig {
    private RetryConfig() {
    }

    public static Retry getRetryConfig(
        RetryStrategy strategy,
        Duration initialDelay,
        int maxAttempts,
        List<Integer> retryCodes
    ) {
        return switch (strategy) {
            case CONSTANT -> Retry.fixedDelay(maxAttempts, initialDelay)
                .filter(throwable -> throwable instanceof WebClientResponseException
                    && retryCodes.contains(((WebClientResponseException) throwable).getStatusCode().value()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());

            case EXPONENTIAL -> Retry.backoff(maxAttempts, initialDelay)
                .filter(throwable -> throwable instanceof WebClientResponseException
                    && retryCodes.contains(((WebClientResponseException) throwable).getStatusCode().value()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());

            case LINEAR -> Retry.from(fl -> fl
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
        };
    }

    public static Retry getRetryConfig(WebClientsConfig.Connection connection) {
        return getRetryConfig(
            connection.strategy(),
            Duration.ofSeconds(connection.delay()),
            connection.attempts().intValue(),
            connection.codes()
        );
    }
}
