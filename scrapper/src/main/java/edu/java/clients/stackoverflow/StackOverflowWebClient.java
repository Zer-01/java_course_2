package edu.java.clients.stackoverflow;

import edu.java.dto.QuestionResponse;
import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class StackOverflowWebClient implements StackoverflowClient {
    private final static String DEFAULT_BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;
    private final static long NUMBER_OF_ATTEMPTS = 5;
    private final static long TIMEOUT_SECONDS = 5;

    public StackOverflowWebClient(String baseUrl) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public StackOverflowWebClient() {
        this(DEFAULT_BASE_URL);
    }

    @Override
    public Optional<QuestionResponse> fetchLastActivity(long questionId) {
        try {
            return webClient.get()
                .uri("/questions/{id}?site=stackoverflow", questionId)
                .retrieve()
                .bodyToMono(QuestionResponse.class)
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .retry(NUMBER_OF_ATTEMPTS)
                .blockOptional();
        } catch (WebClientResponseException e) {
            log.error("StackOverflow client error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
