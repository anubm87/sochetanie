package pro.savichev.bot.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.bot.Bot;
import pro.savichev.db.log.Log;

import java.io.File;

@Component
public class BotImpl implements Bot {
    private static final String TAG = BotImpl.class.getSimpleName();

    private static final int MAX_RETRIES = 5;

    private TelegramBot telegramBot;

    private Log log;

    @Autowired
    public BotImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(Long chatID, String text) {
        SendMessage sendMessage = new SendMessage(chatID, text);
        sendMessage.disableWebPagePreview(true);
        sendMessage.parseMode(ParseMode.HTML);
        send(sendMessage);
    }

    @Override
    public void sendMessage(Long chatID, String text, Boolean disableWebPagePreview) {
        SendMessage sendMessage = new SendMessage(chatID, text);
        sendMessage.disableWebPagePreview(disableWebPagePreview);
        sendMessage.parseMode(ParseMode.HTML);
        send(sendMessage);
    }

    @Override
    public void sendMessage(Long chatID, String text, ParseMode parseMode) {
        SendMessage sendMessage = new SendMessage(chatID, text);
        sendMessage.disableWebPagePreview(true);
        if (parseMode != null) sendMessage.parseMode(parseMode);
        send(sendMessage);
    }

    @Override
    public void sendMessage(Long chatID, String text, ParseMode parseMode, Boolean disableWebPagePreview) {
        SendMessage sendMessage = new SendMessage(chatID, text);
        sendMessage.disableWebPagePreview(disableWebPagePreview);
        if (parseMode != null) sendMessage.parseMode(parseMode);
        send(sendMessage);
    }

    @Override
    public void sendMessage(Long chatID, String text, Keyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatID, text);
        sendMessage.replyMarkup(keyboard);
        send(sendMessage);
    }

    @Override
    public Message sendMessageReturns(Long chatID, String text) {
        SendMessage sendMessage = new SendMessage(chatID, text);
        sendMessage.parseMode(ParseMode.HTML);
        return send(sendMessage);
    }

    @Override
    public Message sendMessageReturns(Long chatID, String text, Keyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatID, text);
        sendMessage.replyMarkup(keyboard);
        return send(sendMessage);
    }

    @Override
    public void answerCallbackQuery(String callbackQueryID, String text) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(callbackQueryID);
        answerCallbackQuery.text(text);
        send(answerCallbackQuery);
    }

    @Override
    public void editMessageText(Long chatID, Integer messageID, String newText) {
        EditMessageText editMessageText = new EditMessageText(chatID, messageID, newText);
        editMessageText.parseMode(ParseMode.HTML);
        send(editMessageText);
    }

    @Override
    public void editMessageReplyMarkup(Long chatID, Integer messageID, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(chatID, messageID);
        editMessageReplyMarkup.replyMarkup(inlineKeyboardMarkup);
        send(editMessageReplyMarkup);
    }


    @Override
    public void sendPhoto(Long chatID, String filePath) {
        SendPhoto sendPhoto = new SendPhoto(chatID, new File(filePath));
        send(sendPhoto);
    }

    @Override
    public void sendPhoto(Long chatID, File file) {
        SendPhoto sendPhoto = new SendPhoto(chatID, file);
        send(sendPhoto);
    }

    @Override
    public void sendPhoto(Long chatID, String filePath, String caption) {
        SendPhoto sendPhoto = new SendPhoto(chatID, new File(filePath));
        if (caption != null) sendPhoto.caption(caption);
        send(sendPhoto);
    }

    @Override
    public void sendPhoto(Long chatID, File file, String caption) {
        SendPhoto sendPhoto = new SendPhoto(chatID, file);
        if (caption != null) sendPhoto.caption(caption);
        send(sendPhoto);
    }

    private <T extends BaseRequest, R extends BaseResponse> void send(final BaseRequest<T, R> request) {
        //noinspection ResultOfMethodCallIgnored
        telegramBot.send(request)
                .retry(MAX_RETRIES)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(R r) {
                        if (!r.isOk()) {
                            log.e(TAG, "Telegram API error: " + r.errorCode() + " - " + r.description());
                        }
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        log.e(TAG, "", e);
                        dispose();
                    }
                });
    }

    private Message send(SendMessage sendMessage) {
        try {
            return telegramBot.send(sendMessage)
                    .retry(MAX_RETRIES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(SendResponse::message)
                    .blockingGet();
        }catch (Throwable e){
            log.e(TAG, "", e);
            return null;
        }
    }




    @Autowired
    public void setLog(Log log) {
        this.log = log;
    }
}
