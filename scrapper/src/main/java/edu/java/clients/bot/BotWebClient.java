package edu.java.clients.bot;

import edu.java.api.exceptions.ApiErrorException;
import edu.java.api.models.ApiErrorResponse;
import edu.java.api.models.LinkUpdateRequest;
import java.time.Duration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotWebClient implements BotClient {
    WebClient webClient;
    private final static long NUMBER_OF_ATTEMPTS = 5;
    private final static long TIMEOUT_SECONDS = 5;

    public BotWebClient(String baseUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base Url cannot be null");
        }
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public void sendUpdate(LinkUpdateRequest request) {
        webClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ApiErrorException(error))))
            .bodyToMono(Void.class)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .retry(NUMBER_OF_ATTEMPTS)
            .block();
    }
}
