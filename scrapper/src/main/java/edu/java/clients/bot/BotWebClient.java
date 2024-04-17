package edu.java.clients.bot;

import edu.java.api.models.ApiErrorResponse;
import edu.java.api.models.LinkUpdateRequest;
import edu.java.configuration.WebClientsConfig;
import edu.java.exceptions.api.ApiErrorException;
import java.time.Duration;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class BotWebClient implements BotClient {
    WebClient webClient;
    private final Retry retryConfig;
    private final long timeoutSeconds;

    public BotWebClient(String baseUrl, long timeout, Retry retryConfig) {
        Objects.requireNonNull(baseUrl, "Base Url cannot be null");
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        timeoutSeconds = timeout;
        this.retryConfig = retryConfig;
    }

    public BotWebClient(WebClientsConfig config, Retry retryConfig) {
        this(config.urls().bot(), config.connection().attempts(), retryConfig);
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
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .retryWhen(retryConfig)
            .block();
    }
}
