package com.week8.finalproject.model.room;

import com.week8.finalproject.model.Timestamped;
import com.week8.finalproject.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class EnterUser extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public EnterUser(User user, Room room) {
        this.user = user;
        this.room = room;
    }

    public EnterUser() {

    }
}
