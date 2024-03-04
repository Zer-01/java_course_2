package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.stackoverflow.StackOverflowWebClient;
import edu.java.dto.QuestionResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
public class StackoverflowClientTest {
    @Test
    void normalResponseTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        long expId = 12345678;
        OffsetDateTime date = OffsetDateTime.of(2024, 2, 25, 14, 50, 29, 0, ZoneOffset.UTC);
        String body =
            String.format(
                "{\"items\":[{\"last_activity_date\":%d,\"question_id\":%d,\"view_count\":134349}]}",
                date.toInstant().getEpochSecond(),
                expId
            );

        stubFor(WireMock.get(String.format("/questions/%d?site=stackoverflow", expId))
            .willReturn(ok().withHeader("content-type", "application/json").withBody(body)));
        StackOverflowWebClient stackOverflowWebClient =
            new StackOverflowWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        Optional<QuestionResponse> response = stackOverflowWebClient.fetchLastActivity(expId);

        assertThat(response)
            .isNotEmpty();
        assertThat(response.get().items())
            .hasSize(1);
        assertEquals(response.get().items().getFirst().id(), expId);
        assertEquals(response.get().items().getFirst().lastActivity(), date);
    }

    @Test
    void questionNotFoundTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        long id = 12345678;
        String body = "{\"items\":[],\"has_more\":false}";

        stubFor(WireMock.get(String.format("/questions/%d?site=stackoverflow", id))
            .willReturn(ok().withHeader("content-type", "application/json").withBody(body)));
        StackOverflowWebClient stackOverflowWebClient =
            new StackOverflowWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        Optional<QuestionResponse> response = stackOverflowWebClient.fetchLastActivity(id);

        assertThat(response)
            .isNotEmpty();
        assertThat(response.get().items())
            .hasSize(0);
    }
}
