package edu.java.configuration;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.github.GitHubWebClient;
import edu.java.clients.stackoverflow.StackOverflowWebClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties
public class ClientConfiguration {
    @Bean
    public GitHubWebClient ghWebClient(BaseUrlsConfig baseUrlsConfig) {
        return new GitHubWebClient(baseUrlsConfig.github());
    }

    @Bean
    public StackOverflowWebClient soWebClient(BaseUrlsConfig baseUrlsConfig) {
        return new StackOverflowWebClient(baseUrlsConfig.stackoverflow());
    }

    @Bean
    public BotWebClient botWebClient(BaseUrlsConfig baseUrlsConfig) {
        return new BotWebClient(baseUrlsConfig.bot());
    }
}
