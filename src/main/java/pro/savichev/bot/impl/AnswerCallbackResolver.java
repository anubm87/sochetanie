package pro.savichev.bot.impl;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import io.reactivex.Completable;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.bot.AnswersInlineKeyboardMaker;
import pro.savichev.bot.Bot;
import pro.savichev.db.sochetanie.model.Answer;
import pro.savichev.db.sochetanie.model.QuestionSent;
import pro.savichev.db.sochetanie.repository.AnswerRepository;
import pro.savichev.db.sochetanie.repository.QuestionSentRepository;

import java.util.Optional;

@Component
public class AnswerCallbackResolver {

    private QuestionSentRepository questionSentRepository;
    private AnswerRepository answerRepository;
    private Bot bot;

    Completable resolve(CallbackQuery callbackQuery) {
        return Completable.fromAction(() -> {
            String data = callbackQuery.data();
            JSONObject object = new JSONObject(data);
            int q = object.getInt("q");
            int a = object.getInt("a");
            Integer userID = callbackQuery.from().id();
            Optional<QuestionSent> questionSentOptional =
                    questionSentRepository.findFirstByUserIdAndQuestionIdOrderByIdDesc(userID, q);
            QuestionSent questionSent = questionSentOptional.orElseThrow(() -> new Exception("QuestionSent not found for user " + " and question " + q));
            if (!questionSent.getComplete()) {
                Optional<Answer> answerOptional = answerRepository.findById(a);
                Answer answer = answerOptional.orElseThrow(() -> new Exception("Answer not found for callback data " + data));
                questionSent.setAnswer(answer);
                questionSentRepository.save(questionSent);
                bot.answerCallbackQuery(callbackQuery.id(), "Ответ \"" + answer.getText() + "\" принят!");
                InlineKeyboardMarkup keyboard = AnswersInlineKeyboardMaker.make(questionSent.getQuestion(), a);
                bot.editMessageReplyMarkup(callbackQuery.from().id().longValue(), questionSent.getMessageId(), keyboard);
            } else {
                bot.answerCallbackQuery(callbackQuery.id(), "Вопрос уже не актуален!");
            }

        });
    }

    @Autowired
    void setQuestionSentRepository(QuestionSentRepository questionSentRepository) {
        this.questionSentRepository = questionSentRepository;
    }

    @Autowired
    void setAnswerRepository(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Autowired
    void setBot(Bot bot) {
        this.bot = bot;
    }
}
