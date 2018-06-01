package pro.savichev.bot.commands.text;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import io.reactivex.Completable;
import org.springframework.stereotype.Component;
import pro.savichev.bot.commands.TextCommand;
import pro.savichev.bot.commands.AbstractCommand;

@Component
@TextCommand("help")
public class Help extends AbstractCommand {
    @Override
    public Completable execute(Message message) {
        return Completable.fromRunnable(() -> bot.sendMessage(message.chat().id(), "Хелп"));
    }
}
