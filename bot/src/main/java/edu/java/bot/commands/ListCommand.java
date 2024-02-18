package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.RequestData;
import edu.java.bot.db.Database;
import java.net.URI;
import java.util.List;

public class ListCommand implements Command {
    Database database;

    public ListCommand(Database database) {
        this.database = database;
    }

    @Override
    public String command() {
        return CommandsEnum.LIST.getCommand();
    }

    @Override
    public String description() {
        return CommandsEnum.LIST.getDescription();
    }

    @Override
    public RequestData handle(Update update) {
        long chatId = update.message().chat().id();
        List<URI> linksList = database.getLinksList(chatId);
        String result;

        if (linksList.isEmpty()) {
            result = "Список отслеживаемых ссылок пуст";
        } else {
            result = urlsListToString(linksList);
        }
        return RequestData.newMessageRequest(chatId, result);
    }

    private String urlsListToString(List<URI> linksList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Список отслеживаемых ссылок:\n");
        for (URI uri : linksList) {
            stringBuilder.append(uri.toString()).append('\n');
        }
        return stringBuilder.toString();
    }
}
