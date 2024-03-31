package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.api.models.LinkUpdateRequest;
import edu.java.clients.bot.BotWebClient;
import edu.java.clients.retry.RetryConfig;
import edu.java.clients.retry.RetryStrategy;
import edu.java.exceptions.api.ApiErrorException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@WireMockTest
public class BotClientTest {
    Retry retryStub = RetryConfig.getRetryConfig(RetryStrategy.CONSTANT, Duration.ofSeconds(2), 3, List.of(500, 501));

    LinkUpdateRequest requestStub =
        new LinkUpdateRequest(1L, URI.create("http://url.com/123"), "description", List.of(1L, 2L, 3L));

    @Test
    void normalResponseTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        stubFor(WireMock.post("/updates")
            .willReturn(ok()));
        BotWebClient botWebClient = new BotWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, retryStub);

        assertDoesNotThrow(() -> botWebClient.sendUpdate(requestStub));
    }

    @Test
    void badRequestStatusResponseTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        String responseBody = """
            {
              "description": "error",
              "code": "400",
              "exceptionName": "name",
              "exceptionMessage": "message",
              "stacktrace": [
                "s1", "s2"
              ]
            }""";
        stubFor(WireMock.post("/updates")
            .willReturn(badRequest().withHeader("content-type", "application/json").withBody(responseBody)));
        BotWebClient botWebClient = new BotWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, retryStub);

        assertThatExceptionOfType(ApiErrorException.class)
            .isThrownBy(() -> botWebClient.sendUpdate(requestStub));
    }
}
