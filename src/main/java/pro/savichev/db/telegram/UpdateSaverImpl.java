package pro.savichev.db.telegram;

import io.reactivex.*;
import pro.savichev.db.telegram.model.*;
import pro.savichev.db.telegram.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateSaverImpl implements UpdateSaver {

    private UserRepository userRepository;
    private UpdateRepository updateRepository;
    private CallbackRepository callbackRepository;

    @Override
    public Completable save(InternalUpdate update) {
        return Completable.fromAction(() -> {
            InternalUser user = update.user();
            if (user != null) {
                saveUser(user);
            }
            if (update.callback() != null) {
                callbackRepository.save(update.callback());
            }
            updateRepository.save(update);
        });
    }

    private void saveUser(InternalUser user) {
        Optional<InternalUser> optional = userRepository.findById(user.id());
        optional.ifPresent(internalUser -> user.setIsAdmin(internalUser.isAdmin()));
        userRepository.save(user);
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCallbackRepository(CallbackRepository callbackRepository) {
        this.callbackRepository = callbackRepository;
    }

    @Autowired
    public void setUpdateRepository(UpdateRepository updateRepository) {
        this.updateRepository = updateRepository;
    }
}
