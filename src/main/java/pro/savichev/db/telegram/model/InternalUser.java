package pro.savichev.db.telegram.model;


import com.pengrad.telegrambot.model.User;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class InternalUser {
    @Id
    @Column
    private Integer id;
    private String first_name;
    private String last_name;
    private String username;
    private Boolean is_admin = false;

    public InternalUser() { }

    InternalUser(User user) {
        id = user.id();
        first_name = user.firstName();
        last_name = user.lastName();
        username = user.username();
    }

    public Integer id() {
        return id;
    }
    public String firstName() {
        return first_name;
    }
    public String lastName() {
        return last_name;
    }
    public String username() {
        return username;
    }
    public Boolean isAdmin() {
        return is_admin;
    }

    public void setIsAdmin(Boolean is_admin) {
        this.is_admin = is_admin;
    }

}
