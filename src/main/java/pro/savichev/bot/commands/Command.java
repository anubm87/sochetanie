package pro.savichev.bot.commands;

import com.pengrad.telegrambot.model.Message;
import io.reactivex.Completable;

public interface Command {
    Completable execute(Message message);
}
