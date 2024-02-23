package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.db.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandProcessor {
    Map<String, Command> commands;

    @Autowired
    public CommandProcessor(List<Command> commandList) {
        commands = new HashMap<>();

        for (Command command : commandList) {
            commands.put(command.command(), command);
        }
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
