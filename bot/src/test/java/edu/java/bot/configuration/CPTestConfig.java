package edu.java.bot.configuration;

import edu.java.bot.metrics.ProcessedMessagesMetric;
import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import edu.java.bot.service.inmem.InMemLinkService;
import edu.java.bot.service.inmem.InMemUserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "edu.java.bot.commands")
public class CPTestConfig {
    @Bean
    public Map<Long, List<String>> db() {
        return new HashMap<>();
    }

    @Bean
    public UserService userService(Map<Long, List<String>> db) {
        return new InMemUserService(db);
    }

    @Bean
    public LinkService linkService(Map<Long, List<String>> db) {
        return new InMemLinkService(db);
    }

    @Bean
    public ProcessedMessagesMetric processedMessagesMetric(Map<Long, List<String>> db) {
        return Mockito.mock(ProcessedMessagesMetric.class);
    }
}
