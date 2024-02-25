package edu.java.configuration;

import edu.java.clients.github.GHWebClient;
import edu.java.clients.stackoverflow.SOWebClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties
public class ClientConfiguration {
    @Bean
    public GHWebClient ghWebClient(BaseUrlsConfig baseUrlsConfig) {
        return new GHWebClient(baseUrlsConfig.github());
    }

    @Bean
    public SOWebClient soWebClient(BaseUrlsConfig baseUrlsConfig) {
        return new SOWebClient(baseUrlsConfig.stackoverflow());
    }
}
