package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.commands.exceptions.InvalidLinkException;
import edu.java.bot.commands.exceptions.track.LinkAlreadyAddedException;
import edu.java.bot.commands.exceptions.track.TrackInvalidFormatException;
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
            throw new TrackInvalidFormatException();
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            throw new InvalidLinkException();
        }

        if (database.isLinkSaved(chatId, url)) {
            throw new LinkAlreadyAddedException();
        }
        database.addLink(chatId, url);

        return new SendMessageRequest(chatId, "Успешно добавлено в список отслеживаемых ссылок");
    }
}
