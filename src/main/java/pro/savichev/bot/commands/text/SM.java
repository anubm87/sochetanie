package pro.savichev.bot.commands.text;

import com.pengrad.telegrambot.model.Message;
import io.reactivex.Completable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.bot.commands.AdminCommand;
import pro.savichev.bot.commands.TextCommand;
import pro.savichev.db.sochetanie.model.GameUser;
import pro.savichev.db.sochetanie.repository.GameUserRepository;

@Component
@TextCommand("sm")
public class SM extends AdminCommand {

    private GameUserRepository gameUserRepository;

    @Override
    public Completable executeAdmin(Message message) throws Exception {
        return Completable.fromAction(() -> {
            String text = message.text();
            String[] split = text.split(" ");
            if (split.length < 2){
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                sb.append(split[i]);
            }
            Iterable<GameUser> gameUsers = gameUserRepository.findAll();
            for (GameUser gameUser: gameUsers) {
                bot.sendMessage(gameUser.getId().longValue(), sb.toString(), false);
            }

        });
    }

    @Autowired
    public void setGameUserRepository(GameUserRepository gameUserRepository) {
        this.gameUserRepository = gameUserRepository;
    }
}
