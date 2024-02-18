package edu.java.bot.botUtils;

public record SendMessageRequest(long chatId, String message) {
    public static SendMessageRequest newMessageRequest(long chatId, String message) {
        return new SendMessageRequest(chatId, message);
    }
}
