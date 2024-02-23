package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.commands.exceptions.InvalidLinkException;
import edu.java.bot.commands.exceptions.untrack.LinkNotFoundException;
import edu.java.bot.commands.exceptions.untrack.UntrackInvalidFormatException;
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
        return "/untrack";
    }

    @Override
    public String description() {
        return "Удалить ссылку из отслеживаемых";
    }

    @Override
    public SendMessageRequest handle(Update update) {
        long chatId = update.message().chat().id();
        String[] args = update.message().text().split(" ");
        if (args.length != 2) {
            throw new UntrackInvalidFormatException();
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            throw new InvalidLinkException();
        }

        if (!database.isLinkSaved(chatId, url)) {
            throw new LinkNotFoundException();
        }
        database.removeLink(chatId, url);

        return new SendMessageRequest(chatId, "Успешно удалено из списка отслеживаемых ссылок");
    }
}
