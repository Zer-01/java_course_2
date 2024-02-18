package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.db.Database;
import edu.java.bot.validators.URLValidator;
import java.net.URI;

public class TrackCommand implements Command {
    Database database;

    public TrackCommand(Database database) {
        this.database = database;
    }

    @Override
    public String command() {
        return CommandsEnum.TRACK.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.TRACK.getDescription();
    }

    @Override
    public SendMessageRequest handle(Update update) {
        long chatId = update.message().chat().id();
        String[] args = update.message().text().split(" ");
        if (args.length != 2) {
            return SendMessageRequest.newMessageRequest(chatId, "Неверный формат\nДолжно быть: /track <URL>");
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            return SendMessageRequest.newMessageRequest(chatId, "Неверный формат ссылки");
        }

        if (database.isLinkSaved(chatId, url)) {
            return SendMessageRequest.newMessageRequest(chatId, "Ошибка: ссылка уже отслеживается");
        }
        database.addLink(chatId, url);

        return SendMessageRequest.newMessageRequest(chatId, "Успешно добавлено в список отслеживаемых ссылок");
    }
}
