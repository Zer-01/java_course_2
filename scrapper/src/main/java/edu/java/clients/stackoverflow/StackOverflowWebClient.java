package edu.java.clients.stackoverflow;

import edu.java.configuration.WebClientsConfig;
import edu.java.dto.QuestionResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class StackOverflowWebClient implements StackoverflowClient {
    private final WebClient webClient;
    private final long numberOfAttempts;
    private final long timeoutSeconds;

    public StackOverflowWebClient(String baseUrl, long attempts, long timeout) {
        Objects.requireNonNull(baseUrl, "Base Url cannot be null");
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        timeoutSeconds = timeout;
        numberOfAttempts = attempts;
    }

    public StackOverflowWebClient(WebClientsConfig config) {
        this(config.urls().stackoverflow(), config.connection().attempts(), config.connection().timeout());
    }

    @Override
    public Optional<QuestionResponse> fetchLastActivity(long questionId) {
        try {
            return webClient.get()
                .uri("/questions/{id}?site=stackoverflow", questionId)
                .retrieve()
                .bodyToMono(QuestionResponse.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .retry(numberOfAttempts)
                .blockOptional();
        } catch (WebClientResponseException e) {
            log.error("StackOverflow client error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
