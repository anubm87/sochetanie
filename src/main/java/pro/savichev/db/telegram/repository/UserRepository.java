package pro.savichev.db.telegram.repository;

import org.springframework.data.repository.CrudRepository;
import pro.savichev.db.telegram.model.InternalUser;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<InternalUser, Integer> {}
