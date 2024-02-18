package edu.java.bot.botUtils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandProcessor;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.db.InMemBotDB;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotApp implements Bot {
    TelegramBot bot;
    CommandProcessor commandProcessor;

    @Autowired
    public BotApp(ApplicationConfig config, InMemBotDB inMemBotDB) {
        this.bot = new TelegramBot(config.telegramToken());
        this.commandProcessor = new CommandProcessor(inMemBotDB);
    }

    @Override
    public void execute(SendMessageRequest requestData) {
        bot.execute(new SendMessage(requestData.chatId(), requestData.message())
            .parseMode(ParseMode.Markdown)
            .disableWebPagePreview(true));
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update == null || update.message() == null || update.message().text().isEmpty()) {
                continue;
            }

            SendMessageRequest result = commandProcessor.process(update);
            if (result == null) {
                result = SendMessageRequest.newMessageRequest(update.message().chat().id(), "Неизвестная команда");
            }
            execute(result);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        bot.setUpdatesListener(this, new BotExceptionHandler());
        setMenuCommands();
    }

    @Override
    public void close() {
        bot.shutdown();
    }

    private void setMenuCommands() {
        List<BotCommand> commands = commandProcessor.commands().stream()
            .map(Command::toApiCommand)
            .toList();

        bot.execute(new SetMyCommands(commands.toArray(new BotCommand[0])));
    }
}
