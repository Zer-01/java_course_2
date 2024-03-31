package edu.java.bot.api.clients.scrapper;

import edu.java.api.models.AddLinkRequest;
import edu.java.api.models.ApiErrorResponse;
import edu.java.api.models.LinkResponse;
import edu.java.api.models.ListLinksResponse;
import edu.java.bot.api.clients.retry.RetryConfig;
import edu.java.bot.configuration.WebClientsConfig;
import edu.java.bot.exceptions.api.ApiErrorException;
import edu.java.bot.exceptions.commands.chat.ChatAlreadyExistsException;
import edu.java.bot.exceptions.commands.chat.ChatNotFoundException;
import edu.java.bot.exceptions.commands.track.LinkAlreadyAddedException;
import edu.java.bot.exceptions.commands.untrack.LinkNotFoundException;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ScrapperWebClient implements ScrapperClient {
    WebClient webClient;
    private final Retry retryConfig;
    private final long timeoutSeconds;
    private final static String CHAT_ENDPOINT = "/tg-chat/{id}";
    private final static String LINK_ENDPOINT = "/links";
    private final static String HEADER_NAME = "Tg-Chat-Id";

    public ScrapperWebClient(String baseUrl, long timeout, Retry retryConfig) {
        Objects.requireNonNull(baseUrl, "Base Url cannot be null");
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        timeoutSeconds = timeout;
        this.retryConfig = retryConfig;
    }

    public ScrapperWebClient(WebClientsConfig config) {
        this(config.urls().scrapper(), config.connection().attempts(), RetryConfig.getRetryConfig(config.connection()));
    }

    public void newChat(Long chatId) {
        webClient.post()
            .uri(CHAT_ENDPOINT, chatId)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ApiErrorException(error))))
            .onStatus(HttpStatus.CONFLICT::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ChatAlreadyExistsException())))
            .bodyToMono(Void.class)
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .retryWhen(retryConfig)
            .block();
    }

    public void deleteChat(Long chatId) {
        webClient.delete()
            .uri(CHAT_ENDPOINT, chatId)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ApiErrorException(error))))
            .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ChatNotFoundException())))
            .bodyToMono(Void.class)
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .retryWhen(retryConfig)
            .block();
    }

    public Optional<ListLinksResponse> getAllLinks(Long chatId) {
        return webClient.get()
            .uri(LINK_ENDPOINT)
            .header(HEADER_NAME, chatId.toString())
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ApiErrorException(error))))
            .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ChatNotFoundException())))
            .bodyToMono(ListLinksResponse.class)
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .retryWhen(retryConfig)
            .blockOptional();
    }

    public Optional<LinkResponse> addLink(Long chatId, AddLinkRequest link) {
        return webClient.post()
            .uri(LINK_ENDPOINT)
            .header(HEADER_NAME, chatId.toString())
            .bodyValue(link)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ApiErrorException(error))))
            .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ChatNotFoundException())))
            .onStatus(HttpStatus.CONFLICT::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new LinkAlreadyAddedException())))
            .bodyToMono(LinkResponse.class)
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .retryWhen(retryConfig)
            .blockOptional();
    }

    public Optional<LinkResponse> deleteLink(Long chatId, AddLinkRequest link) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINK_ENDPOINT)
            .header(HEADER_NAME, chatId.toString())
            .bodyValue(link)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new ApiErrorException(error))))
            .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> (error.exceptionName().equals("LinkNotFoundException"))
                        ? Mono.error(new LinkNotFoundException()) : Mono.error(new ChatNotFoundException())))
            .bodyToMono(LinkResponse.class)
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .retryWhen(retryConfig)
            .blockOptional();
    }
}
