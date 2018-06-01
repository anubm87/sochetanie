package pro.savichev.bot.commands.text;

import com.pengrad.telegrambot.model.Message;
import io.reactivex.Completable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.bot.commands.AdminCommand;
import pro.savichev.bot.commands.TextCommand;
import pro.savichev.db.sochetanie.repository.GameUserRepository;

@Component
@TextCommand("clean")
public class Clean extends AdminCommand {

    private GameUserRepository gameUserRepository;

    @Override
    public Completable executeAdmin(Message message) {
        return Completable.fromAction(() -> {
            gameUserRepository.deleteAll();
            bot.sendMessage(message.chat().id(), "Почистил.");
        });
    }

    @Autowired
    void setGameUserRepository(GameUserRepository gameUserRepository) {
        this.gameUserRepository = gameUserRepository;
    }
}
