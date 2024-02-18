package edu.java.bot.db;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InMemBotDB implements Database {
    private final Map<Long, List<String>> db;

    public InMemBotDB() {
        this.db = new HashMap<>();
    }

    public Map<Long, List<String>> getDb() {
        return db;
    }

    @Override
    public void addUser(long userId) {
        db.putIfAbsent(userId, new ArrayList<>());
    }

    @Override
    public void addLink(long userId, URI link) {
        db.get(userId).add(link.toString());
    }

    @Override
    public void removeLink(long userId, URI link) {
        db.get(userId).remove(link.toString());
    }

    @Override
    public boolean isLinkSaved(long userId, URI link) {
        return db.get(userId).contains(link.toString());
    }

    @Override
    public List<URI> getLinksList(long userId) {
        return db.get(userId).stream()
            .map(URI::create)
            .toList();
    }
}
