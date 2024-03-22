package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.exceptions.commands.InvalidLinkException;
import edu.java.bot.exceptions.commands.track.TrackInvalidFormatException;
import edu.java.bot.service.LinkService;
import edu.java.bot.validators.URLValidator;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    LinkService linkService;

    @Autowired
    public TrackCommand(LinkService linkService) {
        this.linkService = linkService;
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

        linkService.addLink(chatId, url);

        return new SendMessageRequest(chatId, "Успешно добавлено в список отслеживаемых ссылок");
    }
}
