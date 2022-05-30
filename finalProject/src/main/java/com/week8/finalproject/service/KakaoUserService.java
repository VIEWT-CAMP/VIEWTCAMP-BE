package com.week8.finalproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.week8.finalproject.dto.socialDto.KakaoUserRequestDto;
import com.week8.finalproject.dto.socialDto.KakaoUserResponseDto;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.repository.user.UserRepository;
import com.week8.finalproject.security.UserDetailsImpl;
import com.week8.finalproject.security.jwt.JwtTokenUtils;
import com.week8.finalproject.security.provider.JWTAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTAuthProvider jwtAuthProvider;

    private final String defaultImg = "https://wonhee-s3-bucket.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20220516_173115249.png";
    private final String kakaoDefaultImg = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg";

    // 카카오 로그인
    public KakaoUserResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getAccessToken(code);
        log.info("내토큰은 ? =" + accessToken+ "code는? = " + code);
        System.out.println("내토큰은 ? =" + accessToken + "code는? = " + code);
        KakaoUserRequestDto kakaoUserRequestDto = getKakaoUserInfo(accessToken);

        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserRequestDto);
        return forceLogin(kakaoUser, response);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "d36456d44e5ce2e100d58247bad28faf");
        // 카카오 api 변경 필요
//        body.add("redirect_uri", "http://localhost:3000/oauth/kakao/callback");
//        body.add("redirect_uri", "https://sparta-react-basic-3c934.firebaseapp.com/oauth/kakao/callback");
        body.add("redirect_uri", "https://viewtcamp.com/oauth/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserRequestDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        String profileImg;
        if (jsonNode.get("properties").get("profile_image").equals(kakaoDefaultImg) || jsonNode.get("properties").get("profile_image").asText().equals(kakaoDefaultImg)) {
            profileImg = "https://wonhee-s3-bucket.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20220516_173115249.png";
        } else {
            profileImg = jsonNode.get("properties").get("profile_image").asText();
        }
        return new KakaoUserRequestDto(id, nickname, profileImg);
    }

    private User registerKakaoUserIfNeeded(KakaoUserRequestDto kakaoUserRequestDto) {

        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserRequestDto.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        String profileImg = kakaoUserRequestDto.getProfileImg();

        if (kakaoUser == null) {
            // 회원가입
            String nickname = kakaoUserRequestDto.getNickname();
            String username = kakaoUserRequestDto.getNickname();
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String userPr = null;
            //카카오 프로필 미등록 유저는 뷰트캠프 기본 프로필 이미지로 등록
            if(profileImg == null) {
                profileImg = defaultImg;
            }

            kakaoUser = new User(nickname, username, encodedPassword, kakaoId, userPr, profileImg);
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    // 카카오 강제 로그인
    private KakaoUserResponseDto forceLogin(User kakaoUser, HttpServletResponse response) {
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT토큰 헤더에 생성
        String token = JwtTokenUtils.generateJwtToken(userDetails);

        String nickname = kakaoUser.getUsername();
        response.addHeader("Authorization", "Bearer " + token);

        if (kakaoUser.getUsername().equals("")) {

            boolean result = true;
            return KakaoUserResponseDto.builder()
                    .JWtToken(token)
                    .nickname(nickname)
                    .result(result)
                    .build();

        } else {
            boolean result = false;
            return KakaoUserResponseDto.builder()
                    .JWtToken(token)
                    .nickname(nickname)
                    .result(result)
                    .build();
        }
    }
}