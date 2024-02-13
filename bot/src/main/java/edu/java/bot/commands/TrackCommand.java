package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.validators.URLValidator;
import java.net.URI;

public class TrackCommand implements Command {
    private final static String COMMAND = "/track";
    private final static String DESCRIPTION = "start tracking link";

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        String[] args = update.message().text().split(" ");
        if (args.length != 2) {
            return new SendMessage(update.message().chat().id(), "Invalid format\nIt should be: \\track [URL]");
        }

        URI url = URI.create(args[1]);
        if (!URLValidator.isValidLink(url)) {
            return new SendMessage(update.message().chat().id(), "Invalid url format");
        }

        //TODO add the link to user track list
        return new SendMessage(update.message().chat().id(), "Successfully added to the track list");
    }
}
