package edu.java.service.senders;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.clients.bot.BotClient;
import edu.java.exceptions.api.ApiErrorException;
import edu.java.service.SendUpdateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HttpSendUpdateService implements SendUpdateService {
    private final BotClient botClient;

    @Override
    public void sendUpdates(List<LinkUpdateRequest> requests) {
        for (LinkUpdateRequest request : requests) {
            try {
                botClient.sendUpdate(request);
            } catch (ApiErrorException e) {
                log.error("Send update error:{}", e.getResponse());
            }
        }
    }
}
