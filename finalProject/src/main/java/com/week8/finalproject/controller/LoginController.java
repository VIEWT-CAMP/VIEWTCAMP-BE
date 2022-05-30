package com.week8.finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.week8.finalproject.config.jwt.JwtTokenProvider;
import com.week8.finalproject.dto.socialDto.KakaoUserResponseDto;
import com.week8.finalproject.dto.user.LoginRequestDto;
import com.week8.finalproject.dto.user.SignUpRequestDto;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.service.KakaoUserService;
import com.week8.finalproject.service.NaverUserService;
import com.week8.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUserService kakaoUserService;
    private final NaverUserService naverUserService;

    //회원가입
    @PostMapping("/user/signup")
    public Map<String,Object> userRegister(@Valid @RequestBody SignUpRequestDto signUpRequestDto){

        User user = userService.register(signUpRequestDto);

        Map<String, Object> response = new HashMap<>();
        if(user != null){
            response.put("result","success");
            response.put("message","회원가입 성공");
        }else{
            response.put("result","false");
            response.put("message","회원가입 실패");
        }
        return response;
    }

    //로그인
    @PostMapping("/user/login")
    public Map<String,Object> userLogin(@RequestBody LoginRequestDto loginRequestDto){

        User user = userService.userLoginCheck(loginRequestDto);

        Map<String,Object> response = new HashMap<>();

        response.put("token", jwtTokenProvider.createToken(Long.toString(user.getId()),user.getUsername(),user.getUsername()));
        response.put("userPk",user.getId());
        response.put("message","로그인 성공");

        return response;
    }
    // 카카오 소셜 로그인
    @GetMapping("/user/kakao/callback")
    public ResponseEntity<KakaoUserResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        KakaoUserResponseDto kakaoUserResponseDto = kakaoUserService.kakaoLogin(code, response);
        return ResponseEntity.ok().body(kakaoUserResponseDto);
    }
    //네이버 로그인
    @GetMapping("/user/naver/callback")
    public void naverLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        naverUserService.naverLogin(code, response);
    }
}
