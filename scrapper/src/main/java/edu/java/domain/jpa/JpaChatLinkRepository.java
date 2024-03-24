package edu.java.domain.jpa;

import edu.java.domain.repositories.ChatLinkRepository;
import edu.java.entity.Chat;
import edu.java.entity.Link;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatLinkRepository implements ChatLinkRepository {
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<Link> findLinksOfChat(long chatId) {
        Chat chat = entityManager.find(Chat.class, chatId);
        if (chat == null) {
            return null;
        }
        return chat.getLinks().stream()
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Chat> findChatsOfLink(long linkId) {
        Link link = entityManager.find(Link.class, linkId);
        if (link == null) {
            return null;
        }
        return link.getChats().stream()
            .toList();
    }

    @Transactional
    @Override
    public void addLinkForChat(Chat chat, Link link) {
        chat.addLink(link);
        entityManager.persist(chat);
    }

    @Transactional
    @Override
    public void addLinkForChat(long chatId, long linkId) {
        Chat chat = entityManager.find(Chat.class, chatId);
        Link link = entityManager.find(Link.class, linkId);
        addLinkForChat(chat, link);
    }

    @Transactional
    @Override
    public void removeLinkForChat(Chat chat, Link link) {
        chat.removeLink(link);
        entityManager.persist(chat);
    }

    @Transactional
    @Override
    public void removeLinkForChat(long chatId, long linkId) {
        Chat chat = entityManager.find(Chat.class, chatId);
        Link link = entityManager.find(Link.class, linkId);
        removeLinkForChat(chat, link);
    }
}
