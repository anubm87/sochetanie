package pro.savichev.db.sochetanie.model;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private Boolean is_correct;

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Boolean isCorrect() {
        return is_correct;
    }
}
