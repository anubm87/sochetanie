package pro.savichev.db.sochetanie.model;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String question_video_url;
    private String question_text;
    private Long delay;
    private String answer_video_url;
    private Long answer_delay;
    @OneToOne
    @JoinColumn(name = "answer_1", referencedColumnName = "id")
    private Answer answer1;
    @OneToOne
    @JoinColumn(name = "answer_2", referencedColumnName = "id")
    private Answer answer2;
    @OneToOne
    @JoinColumn(name = "answer_3", referencedColumnName = "id")
    private Answer answer3;


    public Integer getId() {
        return id;
    }

    public String getQuestionVideoUrl() {
        return question_video_url;
    }

    public String getQuestionText() {
        return question_text;
    }

    public Long getDelay() {
        return delay;
    }

    public String getAnswerVideoUrl() {
        return answer_video_url;
    }

    public Answer getAnswer1() {
        return answer1;
    }

    public Answer getAnswer2() {
        return answer2;
    }

    public Answer getAnswer3() {
        return answer3;
    }


    public Long getAnswerDelay() {
        return answer_delay;
    }
}
