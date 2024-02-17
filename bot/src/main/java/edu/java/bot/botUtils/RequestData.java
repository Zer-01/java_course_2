package edu.java.bot.botUtils;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

public record RequestData(BaseRequest<? extends BaseRequest<?, ?>, ? extends BaseResponse> request) {
    public static RequestData newMessageRequest(long chatId, String message) {
        return new RequestData(new SendMessage(chatId, message)
            .parseMode(ParseMode.Markdown)
            .disableWebPagePreview(true)
        );
    }
}
