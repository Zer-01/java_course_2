package edu.java.bot.configuration;

import edu.java.bot.db.Database;
import edu.java.bot.db.InMemBotDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "edu.java.bot.commands")
public class CPTestConfig {
    @Bean
    public InMemBotDB inMemBotDB() {
        return new InMemBotDB();
    }
}
