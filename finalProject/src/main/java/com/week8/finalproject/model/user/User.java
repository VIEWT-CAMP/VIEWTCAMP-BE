package com.week8.finalproject.model.user;

import com.week8.finalproject.dto.roomDto.response.UserIntroduceDto;
import lombok.*;

import javax.persistence.*;


@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String nickname;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String userPr;


    @Column(columnDefinition = "TEXT")
    private String profileImg;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String naverId;



    public User(String nickname, String username, String password, Long kakaoId, String userPr, String profileImg) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.kakaoId = kakaoId;
        this.userPr = userPr;
        this.profileImg = profileImg;
    }

    public void updateImg(String imgPath) {
        this.profileImg = imgPath;
    }
    public void updateUserPr(UserIntroduceDto userIntroduceDto) {
        this.userPr = userIntroduceDto.getUserPr();
    }
}


