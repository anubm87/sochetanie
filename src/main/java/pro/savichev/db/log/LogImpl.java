package pro.savichev.db.log;

import io.reactivex.Completable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class LogImpl implements Log {

    private LogRepository logRepository;

    @Autowired
    public LogImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Override
    public void i(String tag, String message) {
        log(tag, LogType.INFO, message);
    }

    @Override
    public void i(String tag, String message, Throwable throwable) {
        log(tag, LogType.INFO, message, throwable);
    }

    @Override
    public void e(String tag, String message) {
        log(tag, LogType.ERROR, message);
    }

    @Override
    public void e(String tag, String message, Throwable throwable) {
        log(tag, LogType.ERROR, message, throwable);
    }

    private void log(String tag, String type, String message, Throwable throwable) {
        log(tag, type, message + "\n" + getStackTrace(throwable));
    }

    private void log(String tag, String type, String message) {
        LogEntity entity = new LogEntity();
        entity.setTag(tag);
        entity.setType(type);
        entity.setMessage(message);
        entity.setTimestamp(System.currentTimeMillis());
        //noinspection ResultOfMethodCallIgnored
        Completable.create(emitter -> logRepository.save(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "", e);
                        dispose();
                    }
                });
    }

    public String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
