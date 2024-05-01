package edu.java.bot.configuration;

import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import edu.java.bot.service.inmem.InMemLinkService;
import edu.java.bot.service.inmem.InMemUserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import static org.mockito.ArgumentMatchers.any;

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
    public MeterRegistry meterRegistry() {
        MeterRegistry MRMock = Mockito.mock(MeterRegistry.class);
        Mockito.when(MRMock.counter(any())).thenReturn(Mockito.mock(Counter.class));
        return MRMock;
    }
}
