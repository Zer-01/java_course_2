package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    UserService userService;

    @Autowired
    public StartCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Начать диалог";
    }

    @Override
    public SendMessageRequest handle(Update update) {
        userService.registerUser(update.message().chat().id());
        return new SendMessageRequest(update.message().chat().id(), "Здравствуй, пользователь.");
    }
}
