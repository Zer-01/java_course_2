package edu.java.bot.botUtils;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;

public interface Bot extends AutoCloseable, UpdatesListener {
    void execute(RequestData requestData);

    @Override
    int process(List<Update> updates);

    void start();

    @Override
    void close();
}
