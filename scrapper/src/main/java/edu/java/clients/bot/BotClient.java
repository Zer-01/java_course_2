package edu.java.clients.bot;

import edu.java.api.models.LinkUpdateRequest;

public interface BotClient {
    void sendUpdate(LinkUpdateRequest request);
}
