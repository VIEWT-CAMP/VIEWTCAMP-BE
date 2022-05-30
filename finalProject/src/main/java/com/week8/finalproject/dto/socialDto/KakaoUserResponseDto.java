package com.week8.finalproject.dto.socialDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoUserResponseDto {
    private String JWtToken;
    private String nickname;
    private boolean result;
}
