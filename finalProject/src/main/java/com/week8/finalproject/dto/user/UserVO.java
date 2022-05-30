package com.week8.finalproject.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@EqualsAndHashCode(of= {"userCd"})
public abstract class UserVO implements UserDetails {
    private String userCd;
    private String username;
    private String nickname;
    private String password;
}