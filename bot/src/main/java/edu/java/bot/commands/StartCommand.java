package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand implements Command {
    @Override
    public String command() {
        return CommandsEnum.START.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.START.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Здравствуй, пользователь.");
    }
}
