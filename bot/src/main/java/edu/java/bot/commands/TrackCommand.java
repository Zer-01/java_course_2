package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.RequestData;
import edu.java.bot.validators.URLValidator;
import java.net.URI;

public class TrackCommand implements Command {
    @Override
    public String command() {
        return CommandsEnum.TRACK.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.TRACK.getDescription();
    }

    @Override
    public RequestData handle(Update update) {
        String[] args = update.message().text().split(" ");
        if (args.length != 2) {
            return RequestData.newMessageRequest(
                update.message().chat().id(),
                "Неверный формат\nДолжно быть: /track \\[URL]"
            );
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            return RequestData.newMessageRequest(update.message().chat().id(), "Неверный формат ссылки");
        }

        //TODO add the link to user track list
        return RequestData.newMessageRequest(
            update.message().chat().id(),
            "Успешно добавлено в список отслеживаемых ссылок"
        );
    }
}
