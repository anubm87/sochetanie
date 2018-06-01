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
@TextCommand("active")
public class Active extends AdminCommand {

    private GameUserRepository gameUserRepository;

    @Override
    public Completable executeAdmin(Message message) {
        return Completable.fromAction(() -> {
            Iterable<GameUser> gameUserIterable = gameUserRepository.findAllByActiveIsTrue();
            StringBuilder sb = new StringBuilder("Победители: ");
            for (GameUser gameUser : gameUserIterable) {
                sb.append(gameUser.getUser().username() != null ? gameUser.getUser().username() : gameUser.getUser().firstName());
                sb.append(' ');
            }
            for (GameUser gameUser : gameUserIterable) {
                bot.sendMessage(gameUser.getId().longValue(), sb.toString());
                Thread.sleep(10000);
                bot.sendMessage(gameUser.getId().longValue(), "Таня, С ДНЕМ РОЖДЕНИЯ! \uD83C\uDF7B");
                bot.sendMessage(gameUser.getId().longValue(), "https://www.youtube.com/watch?v=t4CWN6ns_1Y", false);
            }

        });
    }

    @Autowired
    void setGameUserRepository(GameUserRepository gameUserRepository) {
        this.gameUserRepository = gameUserRepository;
    }
}
