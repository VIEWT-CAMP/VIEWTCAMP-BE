package com.week8.finalproject.dto.user;

import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private Long id;
    private String nickname;
    private String userPr;
    private String profileImg;


    public UserInfoResponseDto(Long id, String nickname, String userPr, String profileImg) {
        this.id = id;
        this.nickname = nickname;
        this.userPr = userPr;
        this.profileImg = profileImg;
    }
}
