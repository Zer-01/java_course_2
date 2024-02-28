package edu.java.bot.api.controllers;

import edu.java.bot.api.models.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UpdateController {
    @PostMapping("/updates")
    public void linkUpdate(@Validated @RequestBody LinkUpdateRequest request) {
        log.info(request.toString());
    }
}
