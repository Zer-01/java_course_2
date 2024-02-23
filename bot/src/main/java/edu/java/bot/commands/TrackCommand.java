package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.db.Database;
import edu.java.bot.validators.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;

@Component
public class TrackCommand implements Command {
    Database database;

    @Autowired
    public TrackCommand(Database database) {
        this.database = database;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Добавить ссылку в отслеживаемые";
    }

    @Override
    public SendMessageRequest handle(Update update) {
        long chatId = update.message().chat().id();
        String[] args = update.message().text().split(" ");
        if (args.length != 2) {
            return new SendMessageRequest(chatId, "Неверный формат\nДолжно быть: /track <URL>");
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            return new SendMessageRequest(chatId, "Неверный формат ссылки");
        }

        if (database.isLinkSaved(chatId, url)) {
            return new SendMessageRequest(chatId, "Ошибка: ссылка уже отслеживается");
        }
        database.addLink(chatId, url);

        return new SendMessageRequest(chatId, "Успешно добавлено в список отслеживаемых ссылок");
    }
}
