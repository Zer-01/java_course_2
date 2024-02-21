package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.db.Database;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandProcessor {
    Map<String, Command> commands;

    public CommandProcessor(Database database) {
        commands = new HashMap<>();

        commands.put(CommandsEnum.HELP.getCommand(), new HelpCommand());
        commands.put(CommandsEnum.LIST.getCommand(), new ListCommand(database));
        commands.put(CommandsEnum.START.getCommand(), new StartCommand(database));
        commands.put(CommandsEnum.TRACK.getCommand(), new TrackCommand(database));
        commands.put(CommandsEnum.UNTRACK.getCommand(), new UntrackCommand(database));
    }

    public SendMessageRequest process(Update update) {
        String firstWord = update.message().text().split(" ")[0];
        Command command = commands.get(firstWord);
        if (command != null) {
            return command.handle(update);
        } else {
            return null;
        }
    }

    public List<Command> commands() {
        return commands.values().stream().toList();
    }
}
