package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.db.Database;

public class StartCommand implements Command {
    Database database;

    public StartCommand(Database database) {
        this.database = database;
    }

    @Override
    public String command() {
        return CommandsEnum.START.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.START.getDescription();
    }

    @Override
    public SendMessageRequest handle(Update update) {
        database.addUser(update.message().chat().id());
        return SendMessageRequest.newMessageRequest(update.message().chat().id(), "Здравствуй, пользователь.");
    }
}
