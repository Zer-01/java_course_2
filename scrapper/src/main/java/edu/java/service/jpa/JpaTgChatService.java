package edu.java.service.jpa;

import edu.java.domain.repositories.ChatRepository;
import edu.java.entity.Chat;
import edu.java.exceptions.api.ChatAlreadyExistsException;
import edu.java.exceptions.api.ChatNotFoundException;
import edu.java.service.TgChatService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            throw new ChatAlreadyExistsException("Chat is already registered");
        }
        chatRepository.add(new Chat(chatId, null));
    }

    @Override
    public void unregister(long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException("Chat not found");
        }
        chatRepository.remove(chatId);
    }
}
