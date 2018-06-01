package pro.savichev.bot.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.reactivex.Completable;
import pro.savichev.bot.BotTextCommandRouter;
import pro.savichev.bot.BotUpdateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.db.log.Log;

@Component
public class BotUpdateResolverImpl implements BotUpdateResolver {
    private static final String TAG = BotUpdateResolverImpl.class.getSimpleName();

    private BotTextCommandRouter botTextCommandRouter;
    private Log log;

    @Autowired
    private AnswerCallbackResolver answerCallbackResolver;

    @Override
    public Completable resolve(Update update) {
        if (update.callbackQuery() != null) {
            return answerCallbackResolver.resolve(update.callbackQuery());
        }
        Message message = update.message();
        if (message == null) {
            message = update.editedMessage();
        }
        if (message == null) {
            message = update.channelPost();
        }
        if (message == null) {
            message = update.editedChannelPost();
        }
        if (message == null) {
            log.i(TAG, "Message not found in Update: " + update.toString());
            return Completable.complete();
        }
        return resolveMessage(message);
    }

    private Completable resolveMessage(Message message) {
        if (message.text() != null) {
            return botTextCommandRouter.getRoute(message.text())
                    .flatMapCompletable(command -> command.execute(message));
        }
        log.i(TAG, "Action not found for Message: " + message.toString());
        return Completable.complete();
    }


    @Autowired
    public void setBotTextCommandRouter(BotTextCommandRouter botTextCommandRouter) {
        this.botTextCommandRouter = botTextCommandRouter;
    }


    @Autowired
    public void setLog(Log log) {
        this.log = log;
    }
}
