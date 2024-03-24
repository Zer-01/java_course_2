package edu.java.bot.configuration;

import edu.java.bot.api.clients.scrapper.ScrapperClient;
import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import edu.java.bot.service.client.ClientLinkService;
import edu.java.bot.service.client.ClientUserService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken
) {
    @Bean
    public UserService clientUserService(ScrapperClient scrapperClient) {
        return new ClientUserService(scrapperClient);
    }

    @Bean
    public LinkService clientLinkService(ScrapperClient scrapperClient) {
        return new ClientLinkService(scrapperClient);
    }
}
