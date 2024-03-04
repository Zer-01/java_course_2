package edu.java.clients.github;

import edu.java.configuration.WebClientsConfig;
import edu.java.dto.RepositoryResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class GitHubWebClient implements GitHubClient {
    private final WebClient webClient;
    private final long numberOfAttempts;
    private final long timeoutSeconds;

    public GitHubWebClient(String baseUrl, long attempts, long timeout) {
        Objects.requireNonNull(baseUrl, "Base Url cannot be null");
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        timeoutSeconds = timeout;
        numberOfAttempts = attempts;
    }

    public GitHubWebClient(WebClientsConfig config) {
        this(config.urls().github(), config.connection().attempts(), config.connection().timeout());
    }

    @Override
    public Optional<RepositoryResponse> fetchLastActivity(String owner, String repoName) {
        try {
            return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repoName)
                .retrieve()
                .bodyToMono(RepositoryResponse.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .retry(numberOfAttempts)
                .blockOptional();
        } catch (WebClientResponseException e) {
            log.error("GitHUb client error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
