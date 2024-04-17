package edu.java.clients.github;

import edu.java.configuration.WebClientsConfig;
import edu.java.dto.RepositoryResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Slf4j
public class GitHubWebClient implements GitHubClient {
    private final WebClient webClient;
    private final Retry retryConfig;
    private final long timeoutSeconds;

    public GitHubWebClient(String baseUrl, long timeout, Retry retryConfig) {
        Objects.requireNonNull(baseUrl, "Base Url cannot be null");
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        timeoutSeconds = timeout;
        this.retryConfig = retryConfig;
    }

    public GitHubWebClient(WebClientsConfig config, Retry retryConfig) {
        this(config.urls().github(), config.connection().attempts(), retryConfig);
    }

    @Override
    public Optional<RepositoryResponse> fetchLastActivity(String owner, String repoName) {
        try {
            return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repoName)
                .retrieve()
                .bodyToMono(RepositoryResponse.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .retryWhen(retryConfig)
                .blockOptional();
        } catch (WebClientResponseException e) {
            log.error("GitHUb client error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
