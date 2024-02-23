package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
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
        return """
                Список команд:
                */list* - Показать список отслеживаемых ссылок.

                */track* - Добавить ссылку в отслеживаемые.
                (формат: /track <URL>)

                */untrack* - Удалить ссылку из отслеживаемых.
                (формат: /untrack <URL>)""";
    }
}
