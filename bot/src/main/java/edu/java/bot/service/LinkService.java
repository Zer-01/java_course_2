package edu.java.bot.service;

import java.net.URI;
import java.util.List;

public interface LinkService {
    void addLink(long userId, URI link);

    void removeLink(long userId, URI link);

    List<URI> getLinksList(long userId);
}
