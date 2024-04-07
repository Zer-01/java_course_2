package edu.java.producers;

import edu.java.api.models.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdateRequest> template;

    public void send(String topic, LinkUpdateRequest update) {
        template.send(topic, update);
    }
}
