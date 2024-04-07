package edu.java.bot.listeners;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.bot.service.UpdateProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class UpdateListener {
    private final UpdateProcessService processService;

    @KafkaListener(topics = "${app.kafka.updates-topic}")
    public void listen(LinkUpdateRequest data) {
        processService.process(data);
    }
}
