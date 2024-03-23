package edu.java.bot.service.client;

import edu.java.bot.api.clients.scrapper.ScrapperClient;
import edu.java.bot.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientUserService implements UserService {
    private final ScrapperClient scrapperClient;

    @Override
    public void registerUser(long userId) {
        scrapperClient.newChat(userId);
    }

    @Override
    public void unregisterUser(long userId) {
        scrapperClient.deleteChat(userId);
    }
}
