package edu.java.bot.api.clients.scrapper;

import edu.java.api.models.AddLinkRequest;
import edu.java.api.models.LinkResponse;
import edu.java.api.models.ListLinksResponse;
import java.util.Optional;

public interface ScrapperClient {
    void newChat(Long chatId);

    void deleteChat(Long chatId);

    Optional<ListLinksResponse> getAllLinks(Long chatId);

    Optional<LinkResponse> addLink(Long chatId, AddLinkRequest link);

    Optional<LinkResponse> deleteLink(Long chatId, AddLinkRequest link);
}
