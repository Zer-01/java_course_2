package edu.java;

import edu.java.service.UpdateService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LinkUpdaterScheduler {
    private final UpdateService updateService;
    @Value("${app.scheduler.force-check-delay:7500}")
    private Duration delay;

    @Scheduled(fixedDelayString = "${app.scheduler.interval:7500}")
    void update() {
        log.info("Updated");
        updateService.updateLinks(delay.getSeconds());
    }
}
