package edu.java.clients.stackoverflow;

import edu.java.clients.retry.RetryConfig;
import edu.java.configuration.WebClientsConfig;
import edu.java.dto.QuestionResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Slf4j
public class StackOverflowWebClient implements StackoverflowClient {
    private final WebClient webClient;
    private final Retry retryConfig;
    private final long timeoutSeconds;

    public StackOverflowWebClient(String baseUrl, long timeout, Retry retryConfig) {
        Objects.requireNonNull(baseUrl, "Base Url cannot be null");
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        timeoutSeconds = timeout;
        this.retryConfig = retryConfig;
    }

    public StackOverflowWebClient(WebClientsConfig config) {
        this(
            config.urls().stackoverflow(),
            config.connection().attempts(),
            RetryConfig.getRetryConfig(config.connection())
        );
    }

    @Override
    public Optional<QuestionResponse> fetchLastActivity(long questionId) {
        try {
            return webClient.get()
                .uri("/questions/{id}?site=stackoverflow", questionId)
                .retrieve()
                .bodyToMono(QuestionResponse.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .retryWhen(retryConfig)
                .blockOptional();
        } catch (WebClientResponseException e) {
            log.error("StackOverflow client error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
