package edu.java.service.senders;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.producers.ScrapperQueueProducer;
import edu.java.service.SendUpdateService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KafkaSendUpdateService implements SendUpdateService {
    private final ScrapperQueueProducer producer;
    private final String topic;

    @Override
    public void sendUpdates(List<LinkUpdateRequest> requests) {
        for (LinkUpdateRequest request : requests) {
            producer.send(topic, request);
        }
    }
}
