package edu.java.configuration;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.github.GitHubWebClient;
import edu.java.clients.stackoverflow.StackOverflowWebClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties
public class ClientConfiguration {
    @Bean
    public GitHubWebClient ghWebClient(WebClientsConfig webClientsConfig) {
        return new GitHubWebClient(webClientsConfig);
    }

    @Bean
    public StackOverflowWebClient soWebClient(WebClientsConfig webClientsConfig) {
        return new StackOverflowWebClient(webClientsConfig);
    }

    @Bean
    public BotWebClient botWebClient(WebClientsConfig webClientsConfig) {
        return new BotWebClient(webClientsConfig);
    }
}
