package edu.java.clients.github;

import edu.java.dto.RepositoryResponse;
import java.util.Optional;

public interface GitHubClient {
    Optional<RepositoryResponse> fetchLastActivity(String owner, String repoName);
}
