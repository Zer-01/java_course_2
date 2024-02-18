package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.commands.CommandProcessor;
import edu.java.bot.db.InMemBotDB;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandProcessorTest {
    static final long chatId = 1;
    static InMemBotDB db;
    static CommandProcessor commandProcessor;

    @BeforeEach
    void initCP() {
        db = new InMemBotDB();
        db.addUser(chatId);
        commandProcessor = new CommandProcessor(db);
    }

    static Arguments[] commands() {
        return new Arguments[] {
            Arguments.of("/help", """
                Список команд:
                /help - Показать доступные команды
                /list - Показать список отслеживаемых ссылок
                /start - Начать диалог
                /track - Добавить ссылку в отслеживаемые
                /untrack - Удалить ссылку из отслеживаемых"""),
            Arguments.of("/list", "Список отслеживаемых ссылок пуст"),
            Arguments.of("/start", "Здравствуй, пользователь."),
            Arguments.of("/track", "Неверный формат\n" +
                "Должно быть: /track <URL>"),
            Arguments.of("/untrack", "Неверный формат\n" +
                "Должно быть: /untrack <URL>"),
            Arguments.of("/track abc", "Неверный формат ссылки"),
            Arguments.of("/untrack def", "Неверный формат ссылки"),
            Arguments.of("/track https://test.com/link", "Успешно добавлено в список отслеживаемых ссылок"),
            Arguments.of("/untrack https://test.com/link2", "Ошибка: ссылка не найдена в списке отслеживаемых")
        };
    }

    @ParameterizedTest
    @MethodSource("commands")
    void commandsTest(String inMessage, String outMessage) {
        Update update = getMockedUpdate(inMessage);

        String result = commandProcessor.process(update).message();

        assertThat(result)
            .isEqualTo(outMessage);
    }

    @Test
    void unknownCommand() {
        Update update = getMockedUpdate("/abc");

        SendMessageRequest result = commandProcessor.process(update);

        assertThat(result)
            .isNull();
    }

    @Test
    void listCommandTest() {
        Update update = getMockedUpdate("/list");
        db.addLink(1, URI.create("https://testlink.com/link1"));
        db.addLink(1, URI.create("https://testlink.com/link2"));
        db.addLink(1, URI.create("https://testlink.com/link3"));
        String expResult = """
            Список отслеживаемых ссылок:
            https://testlink.com/link1
            https://testlink.com/link2
            https://testlink.com/link3
            """;

        String result = commandProcessor.process(update).message();

        assertThat(result)
            .isEqualTo(expResult);
    }

    Update getMockedUpdate(String message) {
        Update mockUpdate = Mockito.mock(Update.class);
        Mockito.when(mockUpdate.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(mockUpdate.message().chat()).thenReturn(Mockito.mock(Chat.class));

        Mockito.when(mockUpdate.message().text()).thenReturn(message);
        Mockito.when(mockUpdate.message().chat().id()).thenReturn(chatId);

        return mockUpdate;
    }
}
