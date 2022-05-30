//package com.week8.finalproject.service;
//
//import com.week8.finalproject.dto.user.UserReviewDto;
//import com.week8.finalproject.model.room.Room;
//import com.week8.finalproject.model.user.User;
//import com.week8.finalproject.model.user.UserReview;
//import com.week8.finalproject.repository.room.RoomRepository;
//import com.week8.finalproject.repository.user.UserRepository;
//import com.week8.finalproject.repository.user.UserReviewRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class UserServiceTest {
//
//    @Autowired
//    UserService userService;
//    @Autowired
//    UserReviewRepository userReviewRepository;
//
//    @Test
//    @DisplayName("후기 작성하기")
//    void createReview() {
//
//        //given
//        UserReviewDto userReviewDto = new UserReviewDto(
//                "제목입니다",
//                "후기입니다"
//        );
//
//        String username = "작성자";
//
//        //when
//        UserReview userReview = userService.createReview(userReviewDto, username);
//
//        //then
//        UserReview userReview_02 = userReviewRepository.findByUsername(username);
//        Assertions.assertThat(userReview.getUsername()).isEqualTo(userReview_02.getUsername());
//    }
//}