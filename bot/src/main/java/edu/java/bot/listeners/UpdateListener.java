package edu.java.bot.listeners;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.bot.service.UpdateProcessService;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;

@Slf4j
@RequiredArgsConstructor
public class UpdateListener {
    private final UpdateProcessService processService;
    private final Validator validator;

    @RetryableTopic(attempts = "1",
                    kafkaTemplate = "retryableTopicKafkaTemplate",
                    dltTopicSuffix = "${app.kafka.dlq-topic-suffix}")
    @KafkaListener(topics = "${app.kafka.updates-topic}")
    public void listen(LinkUpdateRequest data) {
        var validateResult = validator.validate(data);
        if (!validateResult.isEmpty()) {
            log.error(validateResult.toString());
            throw new ValidationException();
        }
        processService.process(data);
    }

    @DltHandler
    public void handleDltUpdate(LinkUpdateRequest data) {
        log.error("Event on dlt topic, payload={}", data);
    }
}
