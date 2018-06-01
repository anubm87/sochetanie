package pro.savichev.db.sochetanie.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.savichev.db.sochetanie.model.Answer;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Integer> {
}
