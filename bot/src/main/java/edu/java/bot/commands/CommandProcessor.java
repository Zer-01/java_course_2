package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.RequestData;
import java.util.List;

public class CommandProcessor {
    List<Command> commands;

    public CommandProcessor() {
        commands = List.of(
            new HelpCommand(),
            new ListCommand(),
            new StartCommand(),
            new TrackCommand(),
            new UntrackCommand()
        );
    }

    public RequestData process(Update update) {
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
