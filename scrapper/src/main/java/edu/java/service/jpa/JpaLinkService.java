package edu.java.service.jpa;

import edu.java.domain.jpa.JpaChatLinkRepository;
import edu.java.domain.repositories.ChatLinkRepository;
import edu.java.domain.repositories.ChatRepository;
import edu.java.domain.repositories.LinkRepository;
import edu.java.entity.Chat;
import edu.java.entity.Link;
import edu.java.exceptions.api.ChatNotFoundException;
import edu.java.exceptions.api.InvalidLinkException;
import edu.java.exceptions.api.LinkAlreadyTrackingException;
import edu.java.exceptions.api.LinkNotFoundException;
import edu.java.service.LinkService;
import edu.java.validators.URLValidator;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final static String INVALID_LINK = "Invalid link";
    private final static String CHAT_NOT_FOUND = "Chat not found";
    private final static String LINK_NOT_FOUND = "Link not found";
    private final static String LINK_ALREADY_TRACKING = "Link already tracking";
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Override
    public Link add(long chatId, URI url) {
        if (!URLValidator.isValidLink(url)) {
            throw new InvalidLinkException(INVALID_LINK);
        }

        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        Link link = linkRepository.findOrCreate(url);
        List<Chat> chatsOfLink = link.getChats().stream().toList();
        if (chatsOfLink.contains(chat.get())) {
            throw new LinkAlreadyTrackingException(LINK_ALREADY_TRACKING);
        }

        chatLinkRepository.addLinkForChat(chat.get(), link);
        return link;
    }

    @Override
    public Link remove(long chatId, URI url) {
        if (!URLValidator.isValidLink(url)) {
            throw new InvalidLinkException(INVALID_LINK);
        }

        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        Optional<Link> link = linkRepository.findByUrl(url);
        if (link.isEmpty()) {
            throw new LinkNotFoundException(LINK_NOT_FOUND);
        }

        List<Chat> chatsOfLink = link.get().getChats().stream().toList();
        if (!chatsOfLink.contains(chat.get())) {
            throw new LinkNotFoundException(LINK_NOT_FOUND);
        }
        if (chatsOfLink.size() == 1) {
            linkRepository.remove(link.get().getId());
        } else {
            chatLinkRepository.removeLinkForChat(chat.get(), link.get());
        }
        return link.get();
    }

    @Override
    public List<Link> listAll(long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        return chat.get().getLinks().stream().toList();
    }
}
