package edu.java.clients.github;

import edu.java.dto.RepositoryResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class GitHubWebClient implements GitHubClient {
    private final WebClient webClient;

    public GitHubWebClient(String baseUrl) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl == null ? "https://api.github.com" : baseUrl)
            .build();
    }

    public GitHubWebClient() {
        this(null);
    }

    @Override
    public Optional<RepositoryResponse> fetchLastActivity(String owner, String repoName) {
        try {
            return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repoName)
                .retrieve()
                .bodyToMono(RepositoryResponse.class)
                .blockOptional();
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }
    }
}
