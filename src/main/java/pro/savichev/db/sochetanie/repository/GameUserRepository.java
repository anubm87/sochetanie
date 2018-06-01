package pro.savichev.db.sochetanie.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.savichev.db.sochetanie.model.GameUser;


@Repository
public interface GameUserRepository extends CrudRepository<GameUser, Integer> {
    Iterable<GameUser> findAllByActiveIsTrue();
}
