package edu.java.service;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.clients.bot.BotClient;
import edu.java.domain.repositories.ChatLinkRepository;
import edu.java.domain.repositories.LinkRepository;
import edu.java.entity.Chat;
import edu.java.entity.Link;
import edu.java.exceptions.api.ApiErrorException;
import edu.java.service.updates.LinksUpdater;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Convert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BotUpdateService implements UpdateService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinksUpdater updater;
    private final BotClient botClient;

    @Override
    public void updateLinks(long linkUpdateInterval) {
        List<Link> links = linkRepository.findCheckedEarlyThan(OffsetDateTime.now().minusSeconds(linkUpdateInterval));
        List<LinkUpdateRequest> requests = updater.getLinkUpdates(links);
        requests = getChatsForRequests(requests);
        sendUpdates(requests);
        for (Link link : links) {
            linkRepository.update(link);
        }
    }

    public List<LinkUpdateRequest> getChatsForRequests(List<LinkUpdateRequest> requests) {
        List<LinkUpdateRequest> newRequests = new ArrayList<>();

        for (LinkUpdateRequest request : requests) {
            List<Chat> chats = chatLinkRepository.findChatsOfLink(request.id());
            newRequests.add(new LinkUpdateRequest(
                request.id(),
                request.url(),
                request.description(),
                chats.stream()
                    .map(Chat::getId)
                    .toList()
            ));
        }
        return newRequests;
    }

    public void sendUpdates(List<LinkUpdateRequest> requests) {
        for (LinkUpdateRequest request : requests) {
            try {
                botClient.sendUpdate(request);
            } catch (ApiErrorException e) {
                log.error("Send update error:" + e.getResponse());
            }
        }
    }
}
