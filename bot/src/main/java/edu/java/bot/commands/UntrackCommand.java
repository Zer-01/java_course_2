package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.exceptions.commands.InvalidLinkException;
import edu.java.bot.exceptions.commands.untrack.UntrackInvalidFormatException;
import edu.java.bot.service.LinkService;
import edu.java.bot.validators.URLValidator;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    LinkService linkService;

    @Autowired
    public UntrackCommand(LinkService linkService) {
        this.linkService = linkService;
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

        linkService.removeLink(chatId, url);

        return new SendMessageRequest(chatId, "Успешно удалено из списка отслеживаемых ссылок");
    }
}
