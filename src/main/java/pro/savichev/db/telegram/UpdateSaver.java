package pro.savichev.db.telegram;

import io.reactivex.Completable;
import pro.savichev.db.telegram.model.InternalUpdate;

public interface UpdateSaver {
    Completable save(InternalUpdate update);
}
