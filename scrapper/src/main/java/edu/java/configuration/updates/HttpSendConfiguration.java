package edu.java.configuration.updates;

import edu.java.clients.bot.BotClient;
import edu.java.service.SendUpdateService;
import edu.java.service.senders.HttpSendUpdateService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpSendConfiguration {
    @Bean
    public SendUpdateService sendUpdateService(BotClient botClient) {
        return new HttpSendUpdateService(botClient);
    }
}
