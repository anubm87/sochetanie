package pro.savichev.bot;

import com.pengrad.telegrambot.model.Update;
import io.reactivex.Completable;

public interface BotUpdateResolver {
    Completable resolve(Update update);
}
