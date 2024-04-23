package edu.java.bot;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.bot.configuration.KafkaTestConfig;
import edu.java.bot.listeners.UpdateListener;
import edu.java.bot.service.UpdateProcessService;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(classes = KafkaTestConfig.class)
public class KafkaConsumerTest {
    @Autowired
    Validator validator;
    UpdateProcessService updateService;
    UpdateListener listener;

    @BeforeEach
    void before() {
        updateService = Mockito.mock(UpdateProcessService.class);
        listener = new UpdateListener(updateService, validator);
    }

    @Test
    void getUpdateTest() {
        LinkUpdateRequest testRequest =
            new LinkUpdateRequest(1L, URI.create("http://testUri.com"), "desc", List.of(1L, 2L));

        listener.listen(testRequest);

        Mockito.verify(updateService).process(testRequest);
    }

    @Test
    void validationErrorTest() {
        LinkUpdateRequest testRequest =
            new LinkUpdateRequest(1L, URI.create("http://testUri.com"), "desc", List.of());

        assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> listener.listen(testRequest));
        Mockito.verify(updateService, Mockito.never()).process(testRequest);
    }
}
