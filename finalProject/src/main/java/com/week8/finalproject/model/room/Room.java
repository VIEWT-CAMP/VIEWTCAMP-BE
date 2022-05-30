package com.week8.finalproject.model.room;

import com.week8.finalproject.dto.roomDto.request.RoomRequestDto;
import com.week8.finalproject.model.Timestamped;
import com.week8.finalproject.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//implements Serializable 추가하였음
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Room extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roomId;

    @Column
    private String title;

    @Column
    private Long userCount;

    @ManyToOne
    @JoinColumn
    private User user;

    @Column
    private int maxUser;

    @Column(nullable = false)
    private String tag1;

    @Column(nullable = false)
    private String tag2;

    @Column(nullable = false)
    private String tag3;

    @Column(nullable = false)
    private boolean studying;


    public static Room create(RoomRequestDto roomDto, User user, int maxUser) {
        Room room = new Room();
        room.roomId = UUID.randomUUID().toString();
        room.title = roomDto.getTitle();
        room.user = user;
        room.userCount = 0L;
        room.maxUser = maxUser;
        room.tag1 = roomDto.getTag1();
        room.tag2 = roomDto.getTag2();
        room.tag3 = roomDto.getTag3();
        room.studying = false;
        return room;
    }
}
