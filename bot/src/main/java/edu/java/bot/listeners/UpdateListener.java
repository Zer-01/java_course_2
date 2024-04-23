package edu.java.bot.listeners;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.bot.service.UpdateProcessService;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class UpdateListener {
    private final UpdateProcessService processService;
    private final Validator validator;

    @KafkaListener(topics = "${app.kafka.updates-topic}")
    public void listen(LinkUpdateRequest data) {
        var validateResult = validator.validate(data);
        if (!validateResult.isEmpty()) {
            log.error(validateResult.toString());
            throw new ValidationException();
        }
        processService.process(data);
    }
}
