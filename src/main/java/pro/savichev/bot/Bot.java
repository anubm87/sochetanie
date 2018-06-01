package pro.savichev.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;

import java.io.File;

public interface Bot {
    void sendMessage(Long chatID, String text);
    void sendMessage(Long chatID, String text, Boolean disableWebPagePreview);
    void sendMessage(Long chatID, String text, ParseMode parseMode);
    void sendMessage(Long chatID, String text, ParseMode parseMode, Boolean disableWebPagePreview);
    void sendMessage(Long chatID, String text, Keyboard keyboard);
    Message sendMessageReturns(Long chatID, String text);
    Message sendMessageReturns(Long chatID, String text, Keyboard keyboard);
    void answerCallbackQuery(String callbackQueryID, String text);
    void editMessageText(Long chatID, Integer messageID, String newText);
    void editMessageReplyMarkup(Long chatID, Integer messageID, InlineKeyboardMarkup inlineKeyboardMarkup);
    void sendPhoto(Long chatID, String filePath);
    void sendPhoto(Long chatID, File file);
    void sendPhoto(Long chatID, String filePath, String caption);
    void sendPhoto(Long chatID, File file, String caption);
}
