package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.RequestData;

public interface Command {
    String command();

    String description();

    RequestData handle(Update update);

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
