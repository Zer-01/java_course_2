package edu.java.bot.commands;

import lombok.Getter;

@Getter
public enum CommandsEnum {
    HELP("/help", "Показать доступные команды"),
    LIST("/list", "Показать список отслеживаемых ссылок"),
    START("/start", "Начать диалог"),
    TRACK("/track", "Добавить ссылку в отслеживаемые"),
    UNTRACK("/untrack", "Удалить ссылку из отслеживаемых");

    private final String command;
    private final String description;

    CommandsEnum(String command, String description) {
        this.command = command;
        this.description = description;
    }

    @Override
    public String toString() {
        return command + " - " + description;
    }
}
