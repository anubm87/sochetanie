package pro.savichev.db.sochetanie.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.savichev.db.sochetanie.model.Question;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {

}
