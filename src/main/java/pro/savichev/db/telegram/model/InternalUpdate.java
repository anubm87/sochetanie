package pro.savichev.db.telegram.model;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import javax.persistence.*;

@Entity
@Table(name = "updates")
public class InternalUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "from_id", referencedColumnName = "id")
    private InternalUser user;
    private Integer date;
    private String text;
    @OneToOne
    @JoinColumn(name = "callback_id", referencedColumnName = "id")
    private InternalCallback callback;

    public InternalUpdate() { }


    public InternalUpdate(Update update) {
        Message message = update.message();
        if (message == null) {
            message = update.editedMessage();
        }
        if (message == null) {
            message = update.channelPost();
        }
        if (message == null) {
            message = update.editedChannelPost();
        }
        if (message != null) {
            User from = message.from();
            if (from != null) {
                user = new InternalUser(from);
            }
            date = message.date();
            text = message.text();
        }
        if (update.callbackQuery() != null) {
            callback = new InternalCallback(update.callbackQuery());
        }
    }

    public Integer id() {
        return id;
    }

    public void id(Integer id) {
        this.id = id;
    }

    public InternalUser user() {
        return user;
    }

    public Integer date() {
        return date;
    }

    public String text() {
        return text;
    }

    public InternalCallback callback() {
        return callback;
    }

}
