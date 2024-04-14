package edu.java.bot.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessedMessagesMetric {
    private final Counter counter;

    @Autowired
    public ProcessedMessagesMetric(MeterRegistry registry) {
        counter = registry.counter("bot_processed_messages");
    }

    public void increment() {
        counter.increment();
    }
}
