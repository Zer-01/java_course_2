package edu.java.configuration;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.github.GitHubWebClient;
import edu.java.clients.stackoverflow.StackOverflowWebClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import reactor.util.retry.Retry;

@ConfigurationProperties
public class ClientConfiguration {
    @Bean
    public GitHubWebClient ghWebClient(WebClientsConfig webClientsConfig, Retry retryConfig) {
        return new GitHubWebClient(webClientsConfig, retryConfig);
    }

    @Bean
    public StackOverflowWebClient soWebClient(WebClientsConfig webClientsConfig, Retry retryConfig) {
        return new StackOverflowWebClient(webClientsConfig, retryConfig);
    }

    @Bean
    public BotWebClient botWebClient(WebClientsConfig webClientsConfig, Retry retryConfig) {
        return new BotWebClient(webClientsConfig, retryConfig);
    }
}
