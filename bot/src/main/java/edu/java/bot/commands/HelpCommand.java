package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    String response;

    @Autowired
    public HelpCommand(List<Command> commandList) {
        StringBuilder builder = new StringBuilder("Список команд:\n");
        for (Command command : commandList) {
            builder.append('*').append(command.command()).append("* - ").append(command.description()).append('\n');
        }
        response = builder.toString();
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Показать доступные команды";
    }

    @Override
    public SendMessageRequest handle(Update update) {
        return new SendMessageRequest(update.message().chat().id(), getCommandList());
    }

    private String getCommandList() {
        return response;
    }
}
