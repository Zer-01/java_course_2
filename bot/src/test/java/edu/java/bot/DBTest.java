package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.CommandProcessor;
import edu.java.bot.configuration.CPTestConfig;
import edu.java.bot.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CPTestConfig.class)
public class DBTest {
    @Autowired
    Map<Long, List<String>> db;
    @Autowired
    UserService userService;
    @Autowired
    CommandProcessor commandProcessor;

    @BeforeEach
    void clearDB() {
        db.clear();
    }

    @Test
    void dbDataTest() {
        userService.registerUser(1);
        userService.registerUser(2);
        Map<Long, List<String>> expResult = new HashMap<>();
        expResult.put(1L, List.of("https://test.com/link1"));
        expResult.put(2L, List.of("https://test.com/link4"));

        commandProcessor.process(getMockedUpdate(1, "/track https://test.com/link1"));
        commandProcessor.process(getMockedUpdate(1, "/track https://test.com/link2"));
        commandProcessor.process(getMockedUpdate(2, "/track https://test.com/link3"));
        commandProcessor.process(getMockedUpdate(2, "/track https://test.com/link4"));

        commandProcessor.process(getMockedUpdate(1, "/untrack https://test.com/link2"));
        commandProcessor.process(getMockedUpdate(2, "/untrack https://test.com/link3"));

        assertThat(db)
            .containsExactlyInAnyOrderEntriesOf(expResult);
    }

    Update getMockedUpdate(long chatId, String message) {
        Update mockUpdate = Mockito.mock(Update.class);
        Mockito.when(mockUpdate.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(mockUpdate.message().chat()).thenReturn(Mockito.mock(Chat.class));

        Mockito.when(mockUpdate.message().text()).thenReturn(message);
        Mockito.when(mockUpdate.message().chat().id()).thenReturn(chatId);

        return mockUpdate;
    }
}
