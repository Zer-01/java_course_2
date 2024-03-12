package edu.java.domain.repositories;

import edu.java.entity.Chat;
import edu.java.entity.Link;
import java.net.URI;
import java.util.List;

public interface ChatLinkRepository {
    List<Link> findLinksOfChat(long chatId);

    List<Chat> findChatsOfLink(long linkId);

    void addLinkForChat(long chatId, URI link);

    void removeLinkForChat(long chatId, URI link);
}
