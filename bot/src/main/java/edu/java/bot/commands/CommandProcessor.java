package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.db.Database;
import java.util.List;

public class CommandProcessor {
    List<Command> commands;

    public CommandProcessor(Database database) {
        commands = List.of(
            new HelpCommand(),
            new ListCommand(database),
            new StartCommand(database),
            new TrackCommand(database),
            new UntrackCommand(database)
        );
    }

    public SendMessageRequest process(Update update) {
        String firstWord = update.message().text().split(" ")[0];
        for (Command command : commands) {
            if (command.command().equals(firstWord)) {
                return command.handle(update);
            }
        }
        return null;
    }

    public List<Command> commands() {
        return commands;
    }
}
