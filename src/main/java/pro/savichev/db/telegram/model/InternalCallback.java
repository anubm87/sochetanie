package pro.savichev.db.telegram.model;

import com.pengrad.telegrambot.model.CallbackQuery;

import javax.persistence.*;

@Entity
@Table(name = "callback")
public class InternalCallback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String callback_id;
    @ManyToOne
    @JoinColumn(name = "from_id", referencedColumnName = "id")
    private InternalUser user;
    private String data;

    InternalCallback() { }

    InternalCallback(CallbackQuery callbackQuery) {
        user = new InternalUser(callbackQuery.from());
        callback_id = callbackQuery.id();
        data = callbackQuery.data();
    }

    public InternalUser user() {
        return user;
    }

    public String data() {
        return data;
    }
}
