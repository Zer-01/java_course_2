package edu.java.bot.api.clients.scrapper;

import edu.java.api.models.AddLinkRequest;
import edu.java.api.models.ApiErrorResponse;
import edu.java.api.models.LinkResponse;
import edu.java.api.models.ListLinksResponse;
import edu.java.bot.exceptions.api.ApiErrorException;
import edu.java.bot.exceptions.commands.chat.ChatAlreadyExistsException;
import edu.java.bot.exceptions.commands.chat.ChatNotFoundException;
import edu.java.bot.exceptions.commands.track.LinkAlreadyAddedException;
import edu.java.bot.exceptions.commands.untrack.LinkNotFoundException;
import java.time.Duration;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperWebClient implements ScrapperClient {
    WebClient webClient;
    private final static long NUMBER_OF_ATTEMPTS = 5;
    private final static long TIMEOUT_SECONDS = 5;
    private final static String CHAT_ENDPOINT = "/tg-chat/{id}";
    private final static String LINK_ENDPOINT = "/links";
    private final static String HEADER_NAME = "Tg-Chat-Id";

    public ScrapperWebClient(String baseUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base Url cannot be null");
        }
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
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
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .retry(NUMBER_OF_ATTEMPTS)
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
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .retry(NUMBER_OF_ATTEMPTS)
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
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .retry(NUMBER_OF_ATTEMPTS)
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
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .retry(NUMBER_OF_ATTEMPTS)
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
                    .flatMap(error -> Mono.error(new ChatNotFoundException())))
            .onStatus(HttpStatus.CONFLICT::equals, response ->
                response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new LinkNotFoundException())))
            .bodyToMono(LinkResponse.class)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .retry(NUMBER_OF_ATTEMPTS)
            .blockOptional();
    }
}
