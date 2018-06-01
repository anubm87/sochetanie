package pro.savichev.db.sochetanie.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.savichev.db.sochetanie.model.QuestionSent;

import java.util.Optional;

@Repository
public interface QuestionSentRepository extends CrudRepository<QuestionSent, Integer> {
    Optional<QuestionSent> findFirstByUserIdAndQuestionIdOrderByIdDesc(Integer user_id, Integer question_id);
}
