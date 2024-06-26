package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.botUtils.SendMessageRequest;
import edu.java.bot.commands.CommandProcessor;
import edu.java.bot.configuration.CPTestConfig;
import edu.java.bot.service.LinkService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CPTestConfig.class)
public class CommandProcessorTest {
    static final long chatId = 1;
    @Autowired
    LinkService linkService;
    @Autowired
    Map<Long, List<String>> db;
    @Autowired
    CommandProcessor commandProcessor;

    @BeforeEach
    void clearDB() {
        db.clear();
        db.put(chatId, new ArrayList<>());
    }

    static Arguments[] commands() {
        return new Arguments[] {
            Arguments.of("/help", """
                Список команд:
                */list* - Показать список отслеживаемых ссылок
                */start* - Начать диалог
                */track* - Добавить ссылку в отслеживаемые
                */untrack* - Удалить ссылку из отслеживаемых
                """),
            Arguments.of("/list", "Список отслеживаемых ссылок пуст"),
            Arguments.of("/start", "И снова здравствуй, пользователь."),
            Arguments.of("/track", "Неверный формат\nДолжно быть: /track <URL>"),
            Arguments.of("/untrack", "Неверный формат\nДолжно быть: /untrack <URL>"),
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

        Optional<SendMessageRequest> result = commandProcessor.process(update);

        assertThat(result)
            .isNotEmpty();
        assertEquals(result.get().message(), outMessage);
    }

    @Test
    void unknownCommand() {
        Update update = getMockedUpdate("/abc");

        Optional<SendMessageRequest> result = commandProcessor.process(update);

        assertThat(result)
            .isEmpty();
    }

    @Test
    void listCommandTest() {
        Update update = getMockedUpdate("/list");
        linkService.addLink(1, URI.create("https://testlink.com/link1"));
        linkService.addLink(1, URI.create("https://testlink.com/link2"));
        linkService.addLink(1, URI.create("https://testlink.com/link3"));
        String expResult = """
            Список отслеживаемых ссылок:
            https://testlink.com/link1
            https://testlink.com/link2
            https://testlink.com/link3
            """;

        Optional<SendMessageRequest> result = commandProcessor.process(update);

        assertThat(result)
            .isNotEmpty();
        assertEquals(result.get().message(), expResult);
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
