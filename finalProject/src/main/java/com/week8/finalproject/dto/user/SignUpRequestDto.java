package com.week8.finalproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignUpRequestDto {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9].{5,16}$",
            message = "아이디를 6 ~ 16자 내외로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9].{7,16}$",
            message = "비밀번호를 8 ~ 16자 내외로 입력해주세요!")
    private String password;


    private String userPr;
    private String userCategory;
    private String profileImg;

}
