package com.week8.finalproject.dto.user;

import com.week8.finalproject.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
public class FollowingDto {
    private User user;
    private User followingUser;
    private String title;
    private String roomId;


    public FollowingDto(User user, User followingUser, String title, String roomId) {
        this.user = user;
        this.followingUser = followingUser;
        this.title = title;
        this.roomId = roomId;
    }


}


