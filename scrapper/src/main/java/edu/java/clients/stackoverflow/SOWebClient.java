package edu.java.clients.stackoverflow;

import edu.java.dto.QuestionResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class SOWebClient implements StackoverflowClient {
    private final WebClient webClient;

    public SOWebClient(String baseUrl) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl == null ? "https://api.stackexchange.com" : baseUrl)
            .build();
    }

    public SOWebClient() {
        this(null);
    }

    @Override
    public Optional<QuestionResponse> fetchLastActivity(long questionId) {
        try {
            return webClient.get()
                .uri("/questions/{id}?site=stackoverflow", questionId)
                .retrieve()
                .bodyToMono(QuestionResponse.class)
                .blockOptional();
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }
    }
}