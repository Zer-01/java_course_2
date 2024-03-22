package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.service.LinkService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final static String LINKS_LIST_EMPTY_MESSAGE = "Список отслеживаемых ссылок пуст";
    LinkService linkService;

    @Autowired
    public ListCommand(LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessageRequest handle(Update update) {
        long chatId = update.message().chat().id();
        List<URI> linksList = linkService.getLinksList(chatId);
        String result;

        if (linksList.isEmpty()) {
            result = LINKS_LIST_EMPTY_MESSAGE;
        } else {
            result = urlsListToString(linksList);
        }
        return new SendMessageRequest(chatId, result);
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
