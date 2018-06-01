package pro.savichev.bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.json.JSONObject;
import pro.savichev.db.sochetanie.model.Question;

public class AnswersInlineKeyboardMaker {
    public static InlineKeyboardMarkup make(Question question, Integer selectedAnswerId){
        JSONObject obj = new JSONObject();
        obj.put("q", question.getId());
        Integer answerId = question.getAnswer1().getId();
        String answerText = question.getAnswer1().getText();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(!answerId.equals(selectedAnswerId) ? answerText : answerText + "\u2705");
        obj.put("a", answerId);
        inlineKeyboardButton1.callbackData(obj.toString());
        answerId = question.getAnswer2().getId();
        answerText = question.getAnswer2().getText();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(!answerId.equals(selectedAnswerId) ? answerText : answerText + "\u2705");
        obj.remove("a");
        obj.put("a", answerId);
        inlineKeyboardButton2.callbackData(obj.toString());
        answerId = question.getAnswer3().getId();
        answerText = question.getAnswer3().getText();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton(!answerId.equals(selectedAnswerId) ? answerText : answerText + "\u2705");
        obj.remove("a");
        obj.put("a", answerId);
        inlineKeyboardButton3.callbackData(obj.toString());
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{inlineKeyboardButton1},
                new InlineKeyboardButton[]{inlineKeyboardButton2},
                new InlineKeyboardButton[]{inlineKeyboardButton3});
    }
}
