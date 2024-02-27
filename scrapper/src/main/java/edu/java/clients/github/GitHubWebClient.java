package edu.java.clients.github;

import edu.java.dto.RepositoryResponse;
import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class GitHubWebClient implements GitHubClient {
    private final static String DEFAULT_BASE_URL = "https://api.github.com";
    private final WebClient webClient;
    private final static long NUMBER_OF_ATTEMPTS = 5;
    private final static long TIMEOUT_SECONDS = 5;

    public GitHubWebClient(String baseUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base Url cannot be null");
        }
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public GitHubWebClient() {
        this(DEFAULT_BASE_URL);
    }

    @Override
    public Optional<RepositoryResponse> fetchLastActivity(String owner, String repoName) {
        try {
            return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repoName)
                .retrieve()
                .bodyToMono(RepositoryResponse.class)
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .retry(NUMBER_OF_ATTEMPTS)
                .blockOptional();
        } catch (WebClientResponseException e) {
            log.error("GitHUb client error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
