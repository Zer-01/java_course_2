package edu.java.bot.botUtils;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotExceptionHandler implements ExceptionHandler {

    @Override
    public void onException(TelegramException e) {
        if (e.response() != null) {
            log.warn(String.valueOf(e.response().errorCode()));
            log.warn(String.valueOf(e.response().description()));
        } else {
            log.warn(e.getMessage());
        }
    }
}
