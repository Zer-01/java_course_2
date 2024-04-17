package edu.java.scrapper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.configuration.WebClientsConfig;
import edu.java.scrapper.clients.ClientsTestConfig;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ClientsTestConfig.class)
@EnableConfigurationProperties(WebClientsConfig.class)
@ActiveProfiles("test")
@WireMockTest
public class LinearRetryTest {
    @Autowired
    Retry retry;

    WebClient webClient;

    public LinearRetryTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        webClient = WebClient.builder()
            .baseUrl(wireMockRuntimeInfo.getHttpBaseUrl())
            .build();
    }

    @DynamicPropertySource
    static void dynamicProperty(DynamicPropertyRegistry registry) {
        registry.add("clients.connection.strategy", () -> "linear");
        registry.add("clients.connection.attempts", () -> "3");
        registry.add("clients.connection.delay", () -> "2");
        registry.add("clients.connection.codes", () -> "500, 502");
    }

    @Test
    public void successTest() {
        String response = "response";

        stubFor(WireMock.get(urlEqualTo("/test"))
            .inScenario("Test Scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willSetStateTo("State 2")
            .willReturn(aResponse()
                .withStatus(500)));

        stubFor(WireMock.get(urlEqualTo("/test"))
            .inScenario("Test Scenario")
            .whenScenarioStateIs("State 2")
            .willSetStateTo("State 3")
            .willReturn(aResponse()
                .withStatus(502)));

        stubFor(WireMock.get(urlEqualTo("/test"))
            .inScenario("Test Scenario")
            .whenScenarioStateIs("State 3")
            .willReturn(ok().withBody(response)));

        String result = clientGet();

        assertEquals(response, clientGet());
    }

    @Test
    public void failTest() {
        String response = "response";

        stubFor(WireMock.get(urlEqualTo("/test"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(this::clientGet);
    }

    @Test
    public void notRepeatableCodeTest() {
        stubFor(WireMock.get(urlEqualTo("/test"))
            .inScenario("Test Scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willSetStateTo("State 2")
            .willReturn(aResponse()
                .withStatus(404)));

        stubFor(WireMock.get(urlEqualTo("/test"))
            .inScenario("Test Scenario")
            .whenScenarioStateIs("State 2")
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(this::clientGet);
    }

    private String clientGet() {
        return webClient.get()
            .uri("/test")
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                response.bodyToMono(String.class)
                    .flatMap(error -> Mono.error(new TestException())))
            .bodyToMono(String.class)
            .retryWhen(retry)
            .blockOptional()
            .orElseThrow();
    }

    private static class TestException extends RuntimeException {
    }
}
