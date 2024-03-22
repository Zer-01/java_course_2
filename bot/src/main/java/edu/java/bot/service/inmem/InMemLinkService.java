package edu.java.bot.service.inmem;

import edu.java.bot.exceptions.commands.chat.ChatNotFoundException;
import edu.java.bot.exceptions.commands.track.LinkAlreadyAddedException;
import edu.java.bot.exceptions.commands.untrack.LinkNotFoundException;
import edu.java.bot.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class InMemLinkService implements LinkService {
    private final Map<Long, List<String>> db;

    @Override
    public void addLink(long userId, URI link) {
        if (db.containsKey(userId)) {
            if (!db.get(userId).contains(link.toString())) {
                db.get(userId).add(link.toString());
            } else {
                throw new LinkAlreadyAddedException();
            }
        } else {
            throw new ChatNotFoundException();
        }
    }

    @Override
    public void removeLink(long userId, URI link) {
        if (db.containsKey(userId)) {
            if (db.get(userId).contains(link.toString())) {
                db.get(userId).remove(link.toString());
            } else {
                throw new LinkNotFoundException();
            }
        } else {
            throw new ChatNotFoundException();
        }
    }

    @Override
    public List<URI> getLinksList(long userId) {
        if (db.containsKey(userId)) {
            return db.get(userId).stream()
                .map(URI::create)
                .toList();
        } else {
            throw new ChatNotFoundException();
        }
    }
}
