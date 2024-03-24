package edu.java.bot.service.client;

import edu.java.api.models.AddLinkRequest;
import edu.java.api.models.LinkResponse;
import edu.java.bot.api.clients.scrapper.ScrapperClient;
import edu.java.bot.service.LinkService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ClientLinkService implements LinkService {
    private final ScrapperClient scrapperClient;

    @Override
    public void addLink(long userId, URI link) {
        scrapperClient.addLink(userId, new AddLinkRequest(link));
    }

    @Override
    public void removeLink(long userId, URI link) {
        scrapperClient.deleteLink(userId, new AddLinkRequest(link));
    }

    @Override
    public List<URI> getLinksList(long userId) {
        var result = scrapperClient.getAllLinks(userId);
        if (result.isPresent()) {
            return result.get().links().stream()
                .map(LinkResponse::url)
                .toList();
        } else {
            log.error("Get all links error. User id:{}", userId);
            return new ArrayList<>();
        }
    }
}
