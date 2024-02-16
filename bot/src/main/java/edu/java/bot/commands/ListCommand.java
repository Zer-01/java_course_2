package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class ListCommand implements Command {
    @Override
    public String command() {
        return CommandsEnum.LIST.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.LIST.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "list: WIP");
    }
}
