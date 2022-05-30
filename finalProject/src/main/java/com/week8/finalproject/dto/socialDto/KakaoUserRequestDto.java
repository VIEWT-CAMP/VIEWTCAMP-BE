package com.week8.finalproject.dto.socialDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserRequestDto {
    private Long id;
    private String nickname;
    private String profileImg;
}
