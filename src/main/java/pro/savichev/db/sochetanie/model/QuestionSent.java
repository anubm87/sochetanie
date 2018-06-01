package pro.savichev.db.sochetanie.model;

import pro.savichev.db.telegram.model.InternalUser;

import javax.persistence.*;

@Entity
@Table(name = "question_sent")
public class QuestionSent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private InternalUser user;
    private Integer message_id;
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "user_answer", referencedColumnName = "id")
    private Answer answer;
    private Boolean complete = false;

    QuestionSent() {
    }

    public QuestionSent(InternalUser user, Integer message_id, Question question) {
        this.user = user;
        this.message_id = message_id;
        this.question = question;
    }


    public Integer getId() {
        return id;
    }

    public InternalUser getUser() {
        return user;
    }

    public Integer getMessageId() {
        return message_id;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }


    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}
