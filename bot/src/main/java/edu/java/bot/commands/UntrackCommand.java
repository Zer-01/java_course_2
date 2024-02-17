package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.RequestData;
import edu.java.bot.validators.URLValidator;
import java.net.URI;

public class UntrackCommand implements Command {
    @Override
    public String command() {
        return CommandsEnum.UNTRACK.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.UNTRACK.getDescription();
    }

    @Override
    public RequestData handle(Update update) {
        String[] args = update.message().text().split(" ");
        if (args.length != 2) {
            return RequestData.newMessageRequest(
                update.message().chat().id(),
                "Неверный формат\nДолжно быть: /untrack \\[URL]"
            );
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            return RequestData.newMessageRequest(update.message().chat().id(), "Неверный формат ссылки");
        }

        //TODO delete the link from user track list
        return RequestData.newMessageRequest(
            update.message().chat().id(),
            "Успешно удалено из списка отслеживаемых ссылок"
        );
    }
}
