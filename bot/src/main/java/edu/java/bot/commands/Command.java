package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;

public interface Command {
    String command();

    String description();

    SendMessageRequest handle(Update update);

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
