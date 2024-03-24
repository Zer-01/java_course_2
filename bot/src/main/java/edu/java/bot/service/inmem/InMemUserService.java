package edu.java.bot.service.inmem;

import edu.java.bot.exceptions.commands.chat.ChatAlreadyExistsException;
import edu.java.bot.exceptions.commands.chat.ChatNotFoundException;
import edu.java.bot.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemUserService implements UserService {
    private final Map<Long, List<String>> db;

    @Override
    public void registerUser(long userId) {
        if (db.putIfAbsent(userId, new ArrayList<>()) != null) {
            throw new ChatAlreadyExistsException();
        }
    }

    @Override
    public void unregisterUser(long userId) {
        if (db.containsKey(userId)) {
            db.remove(userId);
        } else {
            throw new ChatNotFoundException();
        }
    }
}
