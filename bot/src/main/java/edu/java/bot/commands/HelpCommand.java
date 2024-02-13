package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    private final static String COMMAND = "/help";
    private final static String DESCRIPTION = "print available commands";
    private final static String COMMAND_LIST = """
        List of commands:
        */start* -- start a dialogue
        */help* -- display a list of commands(you are here)
        */track [URL]* -- start tracking link
        */untrack* -- stop tracking link
        */list [URL]* -- show a list of tracked links""";

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), COMMAND_LIST)
            .parseMode(ParseMode.Markdown);
    }
}
