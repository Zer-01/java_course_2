package edu.java.api.controllers;

import edu.java.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final UserService service;

    @PostMapping("/tg-chat/{id}")
    public void newChat(@PathVariable("id") long id) {
        service.newChat(id);
        log.info("Новый чат добавлен");
    }

    @DeleteMapping("/tg-chat/{id}")
    public void deleteChat(@PathVariable("id") long id) {
        service.deleteChat(id);
        log.info("Чат удалён");
    }
}
