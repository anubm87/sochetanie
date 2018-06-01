package pro.savichev.bot;

import com.pengrad.telegrambot.model.Update;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import pro.savichev.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pro.savichev.db.log.Log;
import pro.savichev.db.telegram.UpdateSaver;
import pro.savichev.db.telegram.model.InternalUpdate;

@Controller
public class BotUpdateController {

    private static final String TAG = BotUpdateController.class.getSimpleName();

    private BotUpdateResolver botUpdateResolver;
    private UpdateSaver updateSaver;
    private Log log;

    @PostMapping(Config.BOT_SERVLET_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public void onUpdate(@RequestBody Update update) {
        //noinspection ResultOfMethodCallIgnored
        updateSaver.save(new InternalUpdate(update))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .andThen(botUpdateResolver.resolve(update))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        log.e(TAG, "", e);
                        dispose();
                    }
                });
    }

    @Autowired
    public void setLog(Log log) {
        this.log = log;
    }

    @Autowired
    public void setUpdateSaver(UpdateSaver updateSaver) {
        this.updateSaver = updateSaver;
    }

    @Autowired
    public void setBotUpdateResolver(BotUpdateResolver botUpdateResolver) {
        this.botUpdateResolver = botUpdateResolver;
    }
}
