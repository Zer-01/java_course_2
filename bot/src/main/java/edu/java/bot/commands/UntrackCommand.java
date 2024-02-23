package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.db.Database;
import edu.java.bot.validators.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;

@Component
public class UntrackCommand implements Command {
    Database database;

    @Autowired
    public UntrackCommand(Database database) {
        this.database = database;
    }

    @Override
    public String command() {
        return CommandsEnum.UNTRACK.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.UNTRACK.getDescription();
    }

    @Override
    public SendMessageRequest handle(Update update) {
        long chatId = update.message().chat().id();
        String[] args = update.message().text().split(" ");
        if (args.length != 2) {
            return new SendMessageRequest(chatId, "Неверный формат\nДолжно быть: /untrack <URL>");
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            return new SendMessageRequest(chatId, "Неверный формат ссылки");
        }

        if (!database.isLinkSaved(chatId, url)) {
            return new SendMessageRequest(chatId, "Ошибка: ссылка не найдена в списке отслеживаемых");
        }
        database.removeLink(chatId, url);

        return new SendMessageRequest(chatId, "Успешно удалено из списка отслеживаемых ссылок");
    }
}
