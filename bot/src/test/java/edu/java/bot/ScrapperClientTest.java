package edu.java.bot;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.api.models.AddLinkRequest;
import edu.java.api.models.LinkResponse;
import edu.java.api.models.ListLinksResponse;
import edu.java.bot.api.clients.scrapper.ScrapperWebClient;
import edu.java.bot.exceptions.commands.chat.ChatAlreadyExistsException;
import edu.java.bot.exceptions.commands.chat.ChatNotFoundException;
import edu.java.bot.exceptions.commands.track.LinkAlreadyAddedException;
import edu.java.bot.exceptions.commands.untrack.LinkNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class ScrapperClientTest {
    static final String errorResponseStub = """
        {
          "description": "error",
          "code": "400",
          "exceptionName": "name",
          "exceptionMessage": "message",
          "stacktrace": [
            "s1", "s2"
          ]
        }""";

    @Test
    void newChatSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.post(String.format("/tg-chat/%d", chatId))
            .willReturn(ok()));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertDoesNotThrow(() -> scrapperWebClient.newChat(chatId));
    }

    @Test
    void newChatAlreadyExists(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.post(String.format("/tg-chat/%d", chatId))
            .willReturn(aResponse().withStatus(409).withHeader("content-type", "application/json")
                .withBody(errorResponseStub)));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertThatExceptionOfType(ChatAlreadyExistsException.class)
            .isThrownBy(() -> scrapperWebClient.newChat(chatId));
    }

    @Test
    void deleteChatSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.delete(String.format("/tg-chat/%d", chatId))
            .willReturn(ok()));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertDoesNotThrow(() -> scrapperWebClient.deleteChat(chatId));
    }

    @Test
    void deleteChatNotFound(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.delete(String.format("/tg-chat/%d", chatId))
            .willReturn(notFound().withHeader("content-type", "application/json")
                .withBody(errorResponseStub)));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> scrapperWebClient.deleteChat(chatId));
    }

    @Test
    void getAllLinksSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) throws IOException {
        Long chatId = 1L;
        String body = """
            {
              "links": [
                {
                  "id": 1,
                  "url": "http://link1.com/a"
                },
                {
                  "id": 2,
                  "url": "http://link2.com/b"
                }
              ],
              "size": 2
            }""";
        ObjectMapper mapper = new ObjectMapper();
        ListLinksResponse expResponse = mapper.readValue(body, ListLinksResponse.class);
        stubFor(WireMock.get("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(ok().withHeader("content-type", "application/json")
                .withBody(body)));
        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        Optional<ListLinksResponse> response = scrapperWebClient.getAllLinks(chatId);

        assertThat(response)
            .isNotEmpty();
        assertEquals(response.get(), expResponse);
    }

    @Test
    void getAllLinksNotFound(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.get("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(notFound().withHeader("content-type", "application/json")
                .withBody(errorResponseStub)));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> scrapperWebClient.getAllLinks(chatId));
    }

    @Test
    void addLinkSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) throws IOException {
        Long chatId = 1L;
        String body = """
            {
              "id": 2,
              "url": "http://link1.com/a"
            }""";
        ObjectMapper mapper = new ObjectMapper();
        LinkResponse expResponse = mapper.readValue(body, LinkResponse.class);
        stubFor(WireMock.post("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(ok().withHeader("content-type", "application/json")
                .withBody(body)));
        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);
        AddLinkRequest request = new AddLinkRequest(URI.create("http://link1.com/a"));

        Optional<LinkResponse> response = scrapperWebClient.addLink(chatId, request);

        assertThat(response)
            .isNotEmpty();
        assertEquals(response.get(), expResponse);
    }

    @Test
    void addLinkNotFound(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.post("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(notFound().withHeader("content-type", "application/json")
                .withBody(errorResponseStub)));
        AddLinkRequest request = new AddLinkRequest(URI.create("http://link1.com/a"));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> scrapperWebClient.addLink(chatId, request));
    }

    @Test
    void addLinkAlreadyAdded(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.post("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse().withStatus(409).withHeader("content-type", "application/json")
                .withBody(errorResponseStub)));
        AddLinkRequest request = new AddLinkRequest(URI.create("http://link1.com/a"));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertThatExceptionOfType(LinkAlreadyAddedException.class)
            .isThrownBy(() -> scrapperWebClient.addLink(chatId, request));
    }

    @Test
    void deleteLinkSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) throws IOException {
        Long chatId = 1L;
        String body = """
            {
              "id": 2,
              "url": "http://link1.com/a"
            }""";
        ObjectMapper mapper = new ObjectMapper();
        LinkResponse expResponse = mapper.readValue(body, LinkResponse.class);
        stubFor(WireMock.delete("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(ok().withHeader("content-type", "application/json")
                .withBody(body)));
        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);
        AddLinkRequest request = new AddLinkRequest(URI.create("http://link1.com/a"));

        Optional<LinkResponse> response = scrapperWebClient.deleteLink(chatId, request);

        assertThat(response)
            .isNotEmpty();
        assertEquals(response.get(), expResponse);
    }

    @Test
    void deleteLinkNotFoundChat(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.delete("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(notFound().withHeader("content-type", "application/json")
                .withBody(errorResponseStub)));
        AddLinkRequest request = new AddLinkRequest(URI.create("http://link1.com/a"));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> scrapperWebClient.deleteLink(chatId, request));
    }

    @Test
    void deleteLinkNotFoundLink(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Long chatId = 1L;
        stubFor(WireMock.delete("/links").withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse().withStatus(409).withHeader("content-type", "application/json")
                .withBody(errorResponseStub)));
        AddLinkRequest request = new AddLinkRequest(URI.create("http://link1.com/a"));

        ScrapperWebClient scrapperWebClient = new ScrapperWebClient(wireMockRuntimeInfo.getHttpBaseUrl(), 5, 5);

        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> scrapperWebClient.deleteLink(chatId, request));
    }
}
