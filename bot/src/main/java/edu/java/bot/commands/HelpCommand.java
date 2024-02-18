package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;

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
    public SendMessageRequest handle(Update update) {
        return SendMessageRequest.newMessageRequest(update.message().chat().id(), getCommandList());
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
