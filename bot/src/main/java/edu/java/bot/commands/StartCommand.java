package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.RequestData;

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
    public RequestData handle(Update update) {
        //ToDO user registration
        return RequestData.newMessageRequest(update.message().chat().id(), "Здравствуй, пользователь.");
    }
}
