package edu.java.service.db;

import edu.java.domain.repositories.ChatRepository;
import edu.java.entity.Chat;
import edu.java.exceptions.api.ChatAlreadyExistsException;
import edu.java.exceptions.api.ChatNotFoundException;
import edu.java.service.TgChatService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DBTgChatService implements TgChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(long chatId) {
        try {
            chatRepository.add(new Chat(chatId, null));
        } catch (DuplicateKeyException e) {
            throw new ChatAlreadyExistsException("Chat is already registered");
        }
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