package pro.savichev.bot.commands.text;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import io.reactivex.Completable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.bot.AnswersInlineKeyboardMaker;
import pro.savichev.bot.commands.AdminCommand;
import pro.savichev.bot.commands.TextCommand;
import pro.savichev.db.sochetanie.model.GameUser;
import pro.savichev.db.sochetanie.model.Question;
import pro.savichev.db.sochetanie.model.QuestionSent;
import pro.savichev.db.sochetanie.repository.GameUserRepository;
import pro.savichev.db.sochetanie.repository.QuestionRepository;
import pro.savichev.db.sochetanie.repository.QuestionSentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@TextCommand("send_question")
public class SendQuestion extends AdminCommand {

    private static final long ANSWER_TIME = 20000L;

    private QuestionRepository questionRepository;
    private QuestionSentRepository questionSentRepository;
    private GameUserRepository gameUserRepository;

    @Override
    public Completable executeAdmin(Message message) {
        return Completable.fromAction(() -> {
            String text = message.text();
            String[] split = text.split(" ");
            Long chatID = message.chat().id();
            if (split.length < 2) {
                bot.sendMessage(chatID, "<b>Ошибка.</b> Ну параметр то задай.");
                return;
            }
            int firstLength = split[0].length();
            Integer question_id = Integer.parseInt(text.substring(firstLength + 1));
            Optional<Question> questionOptional = questionRepository.findById(question_id);
            Question question = questionOptional.orElseThrow(() -> {
                bot.sendMessage(chatID, "Нет такого вопроса!");
                return new Exception("Question with id " + question_id + " not found.");
            });
            Iterable<GameUser> gameUserIterable = gameUserRepository.findAll();
            if (!gameUserIterable.iterator().hasNext()) {
                return;
            }
            for (GameUser gameUser : gameUserIterable) {
                bot.sendMessage(gameUser.getUser().id().longValue(), question.getQuestionVideoUrl(), false);
            }
            Thread.sleep(question.getDelay());
            InlineKeyboardMarkup keyboard = AnswersInlineKeyboardMaker.make(question, null);
            List<Long> chatIDs = new ArrayList<>();
            for (GameUser gameUser : gameUserIterable) {
                if (gameUser.getActive()) {
                    Message sentMessage = bot.sendMessageReturns(gameUser.getUser().id().longValue(), question.getQuestionText(), keyboard);
                    if (sentMessage != null) {
                        QuestionSent questionSent = new QuestionSent(gameUser.getUser(), sentMessage.messageId(), question);
                        chatIDs.add(gameUser.getId().longValue());
                        questionSentRepository.save(questionSent);
                    }
                }
            }
            List<Integer> timeMessageIDs = new ArrayList<>();
            for (Long id : chatIDs) {
                Message sentMessage = bot.sendMessageReturns(id, "Осталось ...");
                timeMessageIDs.add(sentMessage.messageId());
            }
            for (long i = ANSWER_TIME; i >= 0; i -= 1000L) {
                if (timeMessageIDs.size() == chatIDs.size()) {
                    for (int j = 0; j < timeMessageIDs.size(); j++) {
                        Long id = chatIDs.get(j);
                        Integer messageId = timeMessageIDs.get(j);
                        bot.editMessageText(id, messageId, "Осталось <b>" + (i / 1000L) + "</b>...");
                    }
                }
                Thread.sleep(1000L);
            }
            List<String> rightUsernames = new ArrayList<>();
            List<String> wrongUsernames = new ArrayList<>();
            List<String> lateUsernames = new ArrayList<>();
            for (GameUser gameUser : gameUserIterable) {
                bot.sendMessage(gameUser.getUser().id().longValue(), question.getAnswerVideoUrl(), false);
                Optional<QuestionSent> questionSentOptional = questionSentRepository.findFirstByUserIdAndQuestionIdOrderByIdDesc(gameUser.getId(), question_id);
                questionSentOptional.ifPresent((questionSent -> {
                    questionSent.setComplete(true);
                    questionSentRepository.save(questionSent);
                    if (questionSent.getAnswer() != null && questionSent.getAnswer().isCorrect()) {
                        rightUsernames.add(gameUser.getUser().username() != null ? gameUser.getUser().username() : gameUser.getUser().firstName());
                    } else if (questionSent.getAnswer() != null && !questionSent.getAnswer().isCorrect()) {
                        wrongUsernames.add(gameUser.getUser().username() != null ? gameUser.getUser().username() : gameUser.getUser().firstName());
                        gameUser.setActive(false);
                        gameUserRepository.save(gameUser);
                        bot.sendMessage(gameUser.getId().longValue(), "Пороблено\uD83D\uDE35");
                    } else if (questionSent.getAnswer() == null) {
                        lateUsernames.add(gameUser.getUser().username() != null ? gameUser.getUser().username() : gameUser.getUser().firstName());
                        gameUser.setActive(false);
                        gameUserRepository.save(gameUser);
                    }
                }));
            }
            StringBuilder right = new StringBuilder("Правильно ответили: ");
            for(String s: rightUsernames){
                right.append(s);
                right.append(' ');
            }
            StringBuilder wrong = new StringBuilder("Пороблено: ");
            for(String s: wrongUsernames){
                wrong.append(s);
                wrong.append(' ');
            }
            StringBuilder late = new StringBuilder("Не успели: ");
            for(String s: lateUsernames){
                late.append(s);
                late.append(' ');
            }
            Thread.sleep(question.getAnswerDelay());
            for (GameUser gameUser: gameUserIterable) {
                bot.sendMessage(gameUser.getId().longValue(), right.toString() + "\n" + wrong.toString() + "\n" + late.toString());
            }
        });
    }


    @Autowired
    void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Autowired
    void setQuestionSentRepository(QuestionSentRepository questionSentRepository) {
        this.questionSentRepository = questionSentRepository;
    }

    @Autowired
    void setGameUserRepository(GameUserRepository gameUserRepository) {
        this.gameUserRepository = gameUserRepository;
    }
}
