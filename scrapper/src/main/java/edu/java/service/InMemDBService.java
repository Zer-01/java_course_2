package edu.java.service;

import edu.java.api.exceptions.ChatAlreadyExistsException;
import edu.java.api.exceptions.ChatNotFoundException;
import edu.java.api.exceptions.LinkAlreadyTrackingException;
import edu.java.api.exceptions.LinkNotFoundException;
import edu.java.api.models.LinkResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemDBService implements UserService {
    private final static String CHAT_NOT_FOUND = "Чат не найден";

    Map<Long, List<URI>> db;

    public InMemDBService() {
        db = new HashMap<>();
    }

    @Override
    public void newChat(long id) {
        if (db.containsKey(id)) {
            throw new ChatAlreadyExistsException("Чат уже зарегистрирован");
        }
        db.put(id, new ArrayList<>());
    }

    @Override
    public void deleteChat(long id) {
        if (!db.containsKey(id)) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }
        db.remove(id);
    }

    @Override
    public List<URI> getAllLinks(long chatId) {
        if (!db.containsKey(chatId)) {
            throw new IllegalArgumentException(CHAT_NOT_FOUND);
        }
        return db.get(chatId);
    }

    @Override
    public LinkResponse addLink(long chatId, URI link) {
        if (!db.containsKey(chatId)) {
            throw new IllegalArgumentException(CHAT_NOT_FOUND);
        }

        if (db.get(chatId).contains(link)) {
            throw new LinkAlreadyTrackingException("Ссылка уже отслеживается");
        }
        db.get(chatId).add(link);
        return new LinkResponse((long) link.hashCode(), link);
    }

    @Override
    public LinkResponse deleteLink(long chatId, URI link) {
        if (!db.containsKey(chatId)) {
            throw new IllegalArgumentException(CHAT_NOT_FOUND);
        }

        if (!db.get(chatId).remove(link)) {
            throw new LinkNotFoundException("Ссылка не найдена");
        }
        return new LinkResponse((long) link.hashCode(), link);
    }
}
