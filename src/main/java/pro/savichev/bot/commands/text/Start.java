package pro.savichev.bot.commands.text;

import com.pengrad.telegrambot.model.Message;
import io.reactivex.Completable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.bot.commands.TextCommand;
import pro.savichev.bot.commands.AbstractCommand;
import pro.savichev.db.sochetanie.model.GameUser;
import pro.savichev.db.sochetanie.repository.GameUserRepository;
import pro.savichev.db.telegram.model.InternalUser;
import pro.savichev.db.telegram.repository.UserRepository;

import java.util.Optional;

@Component
@TextCommand("start")
public class Start extends AbstractCommand {

    private UserRepository userRepository;
    private GameUserRepository gameUserRepository;

    @Override
    public Completable execute(Message message) {
        return Completable.fromAction(() -> {
            bot.sendMessage(message.chat().id(), "https://www.youtube.com/watch?v=xaIHHlyq3fE&feature=youtu.be", false);
            Optional<InternalUser> userOptional = userRepository.findById(message.from().id());
            InternalUser user = userOptional.orElseThrow(() -> new Exception("User " + message.from().id() + " not found."));
            Optional<GameUser> gameUserOptional = gameUserRepository.findById(message.from().id());
            if(!gameUserOptional.isPresent()) {
                GameUser gameUser = new GameUser(user);
                gameUserRepository.save(gameUser);
            }
        } );
    }

    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    void setGameUserRepository(GameUserRepository gameUserRepository) {
        this.gameUserRepository = gameUserRepository;
    }
}
