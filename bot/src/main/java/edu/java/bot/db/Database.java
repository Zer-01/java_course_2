package edu.java.bot.db;

import java.net.URI;
import java.util.List;

public interface Database {
    void addUser(long userId);

    void addLink(long userId, URI link);

    void removeLink(long userId, URI link);

    boolean isLinkSaved(long userId, URI link);

    List<URI> getLinksList(long userId);
}
