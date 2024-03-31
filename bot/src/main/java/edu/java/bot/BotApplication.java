package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.ClientConfiguration;
import edu.java.bot.configuration.WebClientsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({ApplicationConfig.class, WebClientsConfig.class, ClientConfiguration.class})
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
