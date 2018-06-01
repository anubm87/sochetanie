package pro.savichev.db.sochetanie.model;

import pro.savichev.db.telegram.model.InternalUser;

import javax.persistence.*;

@Entity
@Table(name = "game_user")
public class GameUser {
    @Id
    @Column
    private Integer id;
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private InternalUser user;
    private Boolean active = true;

    GameUser() {}

    public GameUser(InternalUser user) {
        this.id = user.id();
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public InternalUser getUser() {
        return user;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
