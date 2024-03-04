package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.exceptions.commands.CommandException;
import edu.java.bot.exceptions.commands.InvalidLinkException;
import edu.java.bot.exceptions.commands.track.LinkAlreadyAddedException;
import edu.java.bot.exceptions.commands.track.TrackInvalidFormatException;
import edu.java.bot.exceptions.commands.untrack.LinkNotFoundException;
import edu.java.bot.exceptions.commands.untrack.UntrackInvalidFormatException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public Optional<SendMessageRequest> process(Update update) {
        String firstWord = update.message().text().split(" ")[0];
        Command command = commands.get(firstWord);

        try {
            if (command != null) {
                return Optional.of(command.handle(update));
            } else {
                return Optional.empty();
            }
        } catch (CommandException e) {
            return Optional.of(errorHandle(e, update.message().chat().id()));
        }
    }

    public List<Command> commands() {
        return commands.values().stream().toList();
    }

    private SendMessageRequest errorHandle(CommandException e, long chatId) {
        String errorMessage = switch (e) {
            case InvalidLinkException ile -> "Неверный формат ссылки";
            case TrackInvalidFormatException tife -> "Неверный формат\nДолжно быть: /track <URL>";
            case LinkAlreadyAddedException laae -> "Ошибка: ссылка уже отслеживается";
            case LinkNotFoundException lnfe -> "Ошибка: ссылка не найдена в списке отслеживаемых";
            case UntrackInvalidFormatException uife -> "Неверный формат\nДолжно быть: /untrack <URL>";
            default -> "Неизвестная ошибка";
        };

        return new SendMessageRequest(chatId, errorMessage);
    }
}
