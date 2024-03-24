package edu.java.domain.repositories;

import edu.java.entity.Chat;
import edu.java.entity.Link;
import java.util.List;

public interface ChatLinkRepository {
    List<Link> findLinksOfChat(long chatId);

    List<Chat> findChatsOfLink(long linkId);

    void addLinkForChat(Chat chat, Link link);

    void addLinkForChat(long chatId, long linkId);

    void removeLinkForChat(Chat chat, Link link);

    void removeLinkForChat(long chatId, long linkId);
}
