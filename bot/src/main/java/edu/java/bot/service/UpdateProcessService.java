package edu.java.bot.service;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.bot.botUtils.Bot;
import edu.java.bot.botUtils.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UpdateProcessService {
    private final Bot botApp;

    public void process(LinkUpdateRequest request) {
        List<SendMessageRequest> messages = request.chatIds().stream()
            .map(id -> new SendMessageRequest(id, request.url().toString() + '\n' + request.description()))
            .toList();

        for (SendMessageRequest messageRequest : messages) {
            botApp.execute(messageRequest);
        }
    }
}
