package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    @Override
    public String command() {
        return CommandsEnum.HELP.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.HELP.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), getCommandList())
            .parseMode(ParseMode.Markdown);
    }

    private String getCommandList() {
        return "Список команд:\n"
            + CommandsEnum.HELP.toString() + '\n'
            + CommandsEnum.LIST.toString() + '\n'
            + CommandsEnum.START.toString() + '\n'
            + CommandsEnum.TRACK.toString() + '\n'
            + CommandsEnum.UNTRACK.toString();
    }
}
