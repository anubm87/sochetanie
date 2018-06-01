package pro.savichev.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.savichev.db.telegram.model.InternalUser;
import pro.savichev.db.telegram.repository.UserRepository;

import java.util.Optional;
@Component
public abstract class AdminCommand extends AbstractCommand {

    private UserRepository userRepository;

    @Override
    public Completable execute(Message message) {
        return Single.fromCallable(() -> {
            User from = message.from();
            if (from == null) {
                return Completable.error(new Exception("Admin command from unknown user."));
            }
            Optional<InternalUser> optional = userRepository.findById(message.from().id());
            if (optional.isPresent()) {
                if (optional.get().isAdmin()) {
                    return executeAdmin(message);
                } else {
                    return Completable.error(new Exception("Trying to execute admin command from non-admin user " + optional.get().id() + "."));
                }
            } else {
                return Completable.error(new Exception("User not found."));
            }
        }).flatMapCompletable(completable -> completable);
    }

    public abstract Completable executeAdmin(Message message) throws Exception;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
