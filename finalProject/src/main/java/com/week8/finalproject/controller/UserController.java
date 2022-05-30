package com.week8.finalproject.controller;

import com.week8.finalproject.dto.roomDto.request.QuestionRequestDto;
import com.week8.finalproject.dto.roomDto.response.QuestionResponseDto;
import com.week8.finalproject.dto.roomDto.response.UserIntroduceDto;
import com.week8.finalproject.dto.user.FollowRequestDto;
import com.week8.finalproject.dto.user.UserInfoResponseDto;
import com.week8.finalproject.dto.user.UserReviewDto;
import com.week8.finalproject.model.user.Following;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.user.UserQuestion;
import com.week8.finalproject.model.user.UserReview;
import com.week8.finalproject.security.UserDetailsImpl;
import com.week8.finalproject.service.S3Service;
import com.week8.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController

public class UserController {

    private final UserService userService;
    private final S3Service s3Service;

    // 마이페이지 내 정보 불러오기
    @GetMapping("/user-myinfo")
    public ResponseEntity<UserInfoResponseDto> myinfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(userService.myinfo(userDetails));
    }

    // 자기소개 이미지 수정
    @PutMapping("/user-image/change")
    public void updateImage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value="file") MultipartFile file){
        String imgPath = s3Service.upload(file);
        userService.updateImage(userDetails, imgPath);
    }

    // 자기소개 수정
    @PutMapping("/user-introduce/change")
    public void updateIntroduce(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserIntroduceDto userIntroduceDto){
        userService.updateIntroduce(userDetails, userIntroduceDto);
    }


    //마이페이지 나의 예상질문 작성하기
    @PostMapping("/user-question")
    public ResponseEntity<QuestionResponseDto> createQuestion(@RequestBody QuestionRequestDto questionRequestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok().body(userService.createQuestion(questionRequestDto, userDetails));
    }
    //마이페이지 나의 예상질문 조회하기
   @GetMapping("/user-questions")
   public List<UserQuestion> getQuestion(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getQuestion(userDetails);
   }

    // 마이페이지 나의 예상질문 삭제하기
    @DeleteMapping("/user-questions/{id}")
    public Long deleteQuestion(@PathVariable Long id) {
        return userService.deleteQuestion(id);
    }


    // 나의 소감 조회
    @GetMapping("/user-reviews")
    @ResponseBody
    public List<UserReview> createReview(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getReview(userDetails);
    }
    // 나의 소감 생성
    @PostMapping("/user-reviews")
    public UserReview createReview(@RequestBody UserReviewDto userReviewDto,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return userService.createReview(userReviewDto,username);
    }
    // 나의 소감 수정
    @PatchMapping("/user-reviews/{postId}")
    public Long updateReview(@PathVariable Long postId, @RequestBody UserReviewDto userReviewDto){
        userService.updateReview(postId, userReviewDto);
        return postId;
    }

    // 나의 소감 삭제
    @DeleteMapping("/user-reviews/{postId}")
    public Long deleteReview(@PathVariable Long postId){
        return userService.deleteReview(postId);
    }

    // 팔로우 하기
    @PostMapping("/user-following")
    public Following following(@RequestBody FollowRequestDto followingRequestDto,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.following(followingRequestDto, userDetails);
    }

    // 팔로우한 유저 조회
    @GetMapping("/user-following")
    public ResponseEntity<List<Following>> getFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(userService.getFollowing(userDetails));
    }

    // 팔로워 조회
    @GetMapping("/user-follower")
    public List<Following> getFollower(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getFollower(userDetails);
    }

    // 언팔로우 하기
    @DeleteMapping("/user-unfollowing/{id}")
    public Long unfollowing(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.unfollowing(id,userDetails);
    }





}
