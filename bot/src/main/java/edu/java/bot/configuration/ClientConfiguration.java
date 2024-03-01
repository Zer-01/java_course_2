package edu.java.bot.configuration;

import edu.java.bot.api.clients.scrapper.ScrapperWebClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties
public class ClientConfiguration {
    @Bean
    public ScrapperWebClient scrapperWebClient(BaseUrlsConfig baseUrlsConfig) {
        return new ScrapperWebClient(baseUrlsConfig.scrapper());
    }
}
