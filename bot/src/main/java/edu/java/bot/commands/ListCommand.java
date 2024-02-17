package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.RequestData;

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
    public RequestData handle(Update update) {
        return RequestData.newMessageRequest(update.message().chat().id(), "list: WIP");
    }
}
