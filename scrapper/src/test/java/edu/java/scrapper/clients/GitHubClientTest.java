package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.github.GitHubWebClient;
import edu.java.configuration.WebClientsConfig;
import edu.java.dto.RepositoryResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ClientsTestConfig.class)
@EnableConfigurationProperties(WebClientsConfig.class)
@ActiveProfiles("test")
@WireMockTest()
public class GitHubClientTest {
    @Autowired
    Retry retryStub;

    @Test
    void normalResponseTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        long expId = 123456;
        OffsetDateTime date = OffsetDateTime.of(2024, 2, 25, 14, 50, 29, 0, ZoneOffset.UTC);

        String body = "{\"id\": 123456, \"updated_at\": \"2024-02-25T14:50:29Z\", \"node_id\": \"rand_str\"}";
        stubFor(WireMock.get("/repos/owner/repo")
            .willReturn(ok().withHeader("content-type", "application/json").withBody(body)));
        GitHubWebClient githubClient = new GitHubWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, retryStub);

        Optional<RepositoryResponse> response = githubClient.fetchLastActivity("owner", "repo");

        assertThat(response)
            .isNotEmpty();
        assertEquals(response.get().id(), expId);
        assertEquals(response.get().lastUpdate(), date);
    }

    @Test
    void reposNotFoundTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        String body = "{\"message\": \"Not Found\"}";
        stubFor(WireMock.get("/repos/owner/unknown")
            .willReturn(notFound().withHeader("content-type", "application/json").withBody(body)));
        GitHubWebClient githubClient = new GitHubWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, retryStub);

        Optional<RepositoryResponse> response = githubClient.fetchLastActivity("owner", "unknown");

        assertThat(response)
            .isEmpty();
    }
}
