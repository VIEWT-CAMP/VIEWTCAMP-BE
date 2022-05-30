package com.week8.finalproject.model.user;

import com.week8.finalproject.model.room.EnterUser;
import com.week8.finalproject.model.room.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Following {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private User followingUser;

    @Column
    private boolean followUp;

    @Column
    private String title;

    @Column
    private String roomId;

    @Column
    private boolean studying;

    @Column
    private Long maxUser;

    @Column
    private Long userCount;


    public Following(User user, User followingUser, boolean followUp) {
        this.user = user;
        this.followingUser = followingUser;
        this.followUp = followUp;
    }
//    , boolean studying, Long maxUser, Long userCount
    public void update(String title, String roomUUId, boolean studying, Long maxUser, Long userCount) {
        this.title = title;
        this.roomId = roomUUId;
        this.studying = studying;
        this.maxUser = maxUser;
        this.userCount = userCount;
    }

    public void updateFollowUp(boolean followUp) {
        this.followUp = followUp;
    }
}
