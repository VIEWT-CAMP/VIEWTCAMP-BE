package com.week8.finalproject.service;

import com.week8.finalproject.dto.roomDto.response.QuestionResponseDto;
import com.week8.finalproject.dto.user.*;
import com.week8.finalproject.dto.roomDto.request.QuestionRequestDto;
import com.week8.finalproject.dto.roomDto.response.UserIntroduceDto;
import com.week8.finalproject.exception.UserException;
import com.week8.finalproject.exception.UserExceptionType;
import com.week8.finalproject.model.room.EnterUser;
import com.week8.finalproject.model.user.Following;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.user.UserQuestion;
import com.week8.finalproject.model.user.UserReview;
import com.week8.finalproject.repository.room.EnterUserRepository;
import com.week8.finalproject.repository.user.FollowingRepositoy;
import com.week8.finalproject.repository.user.UserQuestionRepository;
import com.week8.finalproject.repository.user.UserRepository;
import com.week8.finalproject.repository.user.UserReviewRepository;
import com.week8.finalproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final UserReviewRepository userReviewRepository;
    private final FollowingRepositoy followingRepositoy;
    private final EnterUserRepository enterUserRepository;

    //회원가입
    @Transactional
    public User register(SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();

        // username 중복검사
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserException(UserExceptionType.ALREADY_EXIST_USERNAME);
        }
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        return userRepository.save(user);
    }

    //로그인 유효성 검사
    public User userLoginCheck(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UserException(UserExceptionType.WRONG_PASSWORD);
        }
        return user;
    }

    // 마이페이지 정보 불러오기
    public UserInfoResponseDto myinfo(UserDetailsImpl userDetails) {
        String myname = userDetails.getUsername();
        User user = userRepository.findByUsername(myname)
                .orElseThrow(() -> new IllegalArgumentException("내 정보가 없습니다."));
        return new UserInfoResponseDto(user.getId(), user.getNickname(), user.getUserPr(), user.getProfileImg());
    }

    // 회원 이미지 수정
    @Transactional
    public void updateImage(UserDetailsImpl userDetails, String imgPath) {
        String myname = userDetails.getUsername();
        User user = userRepository.findByUsername(myname).orElseThrow(
                () -> new IllegalArgumentException("해당되는 유저가 없습니다.")
        );
        user.updateImg(imgPath);
    }

    // 회원 자기소개 수정
    @Transactional
    public void updateIntroduce(UserDetailsImpl userDetails, UserIntroduceDto userIntroduceDto) {
        String myname = userDetails.getUsername();
        User user = userRepository.findByUsername(myname).orElseThrow(
                () -> new IllegalArgumentException("해당되는 유저가 없습니다.")
        );
        user.updateUserPr(userIntroduceDto);
    }

    // 나의 소감 조회
    public List<UserReview> getReview(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return userReviewRepository.findAllByUsernameOrderByCreatedAtDesc(username);
    }

    // 나의 소감 생성
    public UserReview createReview(UserReviewDto userReviewDto, String username) {
        UserReview userReview = new UserReview(userReviewDto, username);
        return userReviewRepository.save(userReview);
    }

    // 나의 소감 수정
    @Transactional
    public void updateReview(Long postId, UserReviewDto userReviewDto) {
        UserReview userReview = userReviewRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 리뷰가 없습니다.")
        );
        userReview.update(userReviewDto);
    }

    // 나의 소감 삭제
    @Transactional
    public Long deleteReview(Long postId) {
        userReviewRepository.deleteById(postId);
        return postId;
    }

    //예상 질문 작성하기
    public QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        List<UserQuestion> findUser = userQuestionRepository.findAllByUser(user);

        if (findUser.size() > 10) {
            throw new IllegalArgumentException("예상질문은 10개까지 작성 가능합니다.");
        } else {
            UserQuestion userQuestion = UserQuestion.userQuestion(questionRequestDto, user);
            UserQuestion createQuestion = userQuestionRepository.save(userQuestion);
            Long id = createQuestion.getId();
            String question = createQuestion.getQuestion();
            LocalDateTime createdAt = createQuestion.getCreatedAt();

         return new QuestionResponseDto(id, question, createdAt);
        }
    }

    // 예상 질문 삭제하기
    @Transactional
    public Long deleteQuestion(Long id) {
        userQuestionRepository.deleteById(id);
        return id;
    }

    // 나의 예상질문 조회하기
    public List<UserQuestion> getQuestion(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return userQuestionRepository.findAllByUser(user);
    }

    // 팔로우 하기
    @Transactional
    public Following following(FollowRequestDto followingRequestDto, UserDetailsImpl userDetails) {
        Long kakaoId = followingRequestDto.getKakaoId();
        String username = userDetails.getUsername();

        //카카오 아이디로 팔로우할 유저 찾기 (동명이인 방지)
        User followingUser = userRepository.findALlByKakaoId(kakaoId);

        //내 user 정보 찾기
        User user = userRepository.findAllByUsername(username);

        //이미 팔로우한 사람인지 확인
        if (followingRepositoy.findByUserAndFollowingUser(user, followingUser) == null) {

            //맞팔 설정
            if (followingRepositoy.findByFollowingUserAndUser(user, followingUser) != null) {
                boolean followUp = true;
                Following following = new Following(user, followingUser, followUp);
                Following following_02 = followingRepositoy.findByFollowingUserAndUser(user, followingUser);
                following_02.updateFollowUp(true);
                return followingRepositoy.save(following);
            }
            // 팔로잉에 내정보랑 팔로우 할 사람의 정보를 저장
            boolean followUp = false;
            Following following = new Following(user, followingUser, followUp);
            return followingRepositoy.save(following);
        } else {
            throw new UserException(UserExceptionType.HAS_FOLLOW_USER);
        }
    }

    // 팔로잉(내가 팔로우한) 유저 조회
    @Transactional
    public List<Following> getFollowing(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        // 내가 팔로우한 사람
        List<Following> following = followingRepositoy.findAllByUser(user);

        for (Following value : following) {
            //내가 팔로우한 사람을 followingUser 변수등록
            User followingUser = value.getFollowingUser();
            // 팔로우한 사람이 스터디중인지 확인하여 스터디중이라면
            if (enterUserRepository.findByUser(followingUser) != null) {
                //내가 팔로잉한 사람이 들어가있는 방을 찾음
                EnterUser enterUser = enterUserRepository.findByUser(followingUser);
                //들어가있는 방의 정보들을 찾아서 정보 저장
                String title = enterUser.getRoom().getTitle();
                String roomId = enterUser.getRoom().getRoomId();
                boolean studying = true;
                Long maxUser = (long) enterUser.getRoom().getMaxUser();
                Long userCount = enterUser.getRoom().getUserCount();
                value.update(title, roomId, studying, maxUser, userCount);
            } else {
                List<Following> following_02 = followingRepositoy.findAllByFollowingUser(followingUser);
                for (Following value_02 : following_02) {
                    value_02.update(null, null, false, null, null);
                }
            }
        }
        return followingRepositoy.findAllByUser(user);
    }

    // 팔로워(나를 팔로우한) 조회하기
    @Transactional
    public List<Following> getFollower(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return followingRepositoy.findAllByFollowingUser(user);
    }

    // 언팔로우
    @Transactional
    public Long unfollowing(Long id, UserDetailsImpl userDetails) {
        User my = userDetails.getUser();
        Following follow = followingRepositoy.findAllById(id); // 336
        //내가 팔로우 하고있던사람.
        User followingUser = follow.getFollowingUser();
        // 팔로잉에서 언팔할때
        Following following = followingRepositoy.findByFollowingUserAndUser(followingUser, my);
        //나를 팔로잉 한사람이 있다면 (팔로워가 있다면)
        System.out.println("내 아이디값 = ? " + my.getId() + "상대방 아이디값 = ? " + followingUser.getId());
        if (followingRepositoy.findByFollowingUserAndUser(my, followingUser) != null) {
            Following following_01 = followingRepositoy.findByFollowingUserAndUser(my, followingUser);
            //팔로워의 맞팔 상태를 false로 바꿔준다.
            following_01.updateFollowUp(false);
            //그리고나서 내 팔로잉 기록을 지운다.
            followingRepositoy.deleteById(following.getId());
        //팔로워에서 언팔할때
        } else {
            if (my.getId().equals(followingUser.getId())) {
                User user_02 = follow.getUser();
                Following following_02 = followingRepositoy.findByFollowingUserAndUser(user_02, my);
                Following following_04 = followingRepositoy.findByFollowingUserAndUser(my, user_02);
                if (followingRepositoy.findByFollowingUserAndUser(my, user_02) != null) {
                    following_04.updateFollowUp(false);
                }
                followingRepositoy.deleteById(following_02.getId());
                return following_02.getId();
            }
            //팔로잉한 사람 제거하여 팔로잉탭에서 제거함.
            followingRepositoy.deleteById(following.getId());
        }
        return id;
    }
}
