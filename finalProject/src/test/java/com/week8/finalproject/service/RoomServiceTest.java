//package com.week8.finalproject.service;
//
//import com.week8.finalproject.dto.roomDto.request.RoomEnterRequestDto;
//import com.week8.finalproject.dto.roomDto.request.RoomRequestDto;
//import com.week8.finalproject.dto.roomDto.response.RoomResponseDto;
//import com.week8.finalproject.model.room.Room;
//import com.week8.finalproject.model.user.User;
//import com.week8.finalproject.model.user.UserQuestion;
//import com.week8.finalproject.repository.room.RoomRepository;
//import com.week8.finalproject.repository.user.UserQuestionRepository;
//import com.week8.finalproject.repository.user.UserRepository;
//import com.week8.finalproject.security.UserDetailsImpl;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Objects;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WithMockUser
//class RoomServiceTest {
//    @Autowired
//    RoomService roomService;
//    @Autowired
//    RoomRepository roomRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    UserQuestionRepository userQuestionRepository;
//
//    @Test
//    @DisplayName("스터디룸 생성")
//    void 스터디룸_생성하기() {
//        //given
//        RoomRequestDto roomRequestDto = new RoomRequestDto();
//        roomRequestDto.setTitle("테스트");
//        roomRequestDto.setRoomId("awd452-3wrf-3wf-fr3-adawd");
//        roomRequestDto.setUserCount(3L);
//        roomRequestDto.setMaxUser(4);
//        roomRequestDto.setTag1("테스트1");
//        roomRequestDto.setTag2("테스트2");
//        roomRequestDto.setTag3("테스트3");
//        roomRequestDto.setStudying(false);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("유저네임");
//        //when
//        RoomResponseDto room = roomService.createRoom(roomRequestDto, user);
//        //then
//        Room room01 = roomRepository.findByTitle(room.getTitle());
//        assertThat(roomRequestDto.getTitle()).isEqualTo(room01.getTitle());
//    }
//
//    @Test
//    @DisplayName("스터디룸 중복 제목")
//    void 스터디룸_중복제목() {
//        //given
//        RoomRequestDto roomRequestDto = new RoomRequestDto();
//        roomRequestDto.setTitle("테스트1");
//        roomRequestDto.setRoomId("awd452-3wrf-3wf-fr3-adawd");
//        roomRequestDto.setUserCount(3L);
//        roomRequestDto.setMaxUser(4);
//        roomRequestDto.setTag1("테스트1");
//        roomRequestDto.setTag2("테스트2");
//        roomRequestDto.setTag3("테스트3");
//        roomRequestDto.setStudying(false);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("유저네임");
//
//        RoomRequestDto roomRequestDto_02 = new RoomRequestDto();
//        roomRequestDto_02.setTitle("테스트1");
//        roomRequestDto_02.setRoomId("efj22sd-234ffs3-fs3f4-22f21342134");
//        roomRequestDto_02.setUserCount(3L);
//        roomRequestDto_02.setMaxUser(4);
//        roomRequestDto_02.setTag1("테스트4");
//        roomRequestDto_02.setTag2("테스트5");
//        roomRequestDto_02.setTag3("테스트6");
//        roomRequestDto_02.setStudying(false);
//
//        User user_02 = new User();
//        user.setId(2L);
//        user.setUsername("유저네임2");
//        //when
//
//        roomService.createRoom(roomRequestDto, user);
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(roomRequestDto_02, user_02));
//        //then
//        assertThat(e.getMessage()).isEqualTo("이미 존재하는 방 이름입니다.");
//    }
//
//    @Test
//    void 스터디룸_제목없을때() {
//        //given
//        RoomRequestDto roomRequestDto = new RoomRequestDto();
////        roomRequestDto.setTitle("테스트");
//        roomRequestDto.setRoomId("awd452-3wrf-3wf-fr3-adawd");
//        roomRequestDto.setUserCount(3L);
//        roomRequestDto.setMaxUser(4);
//        roomRequestDto.setTag1("테스트1");
//        roomRequestDto.setTag2("테스트2");
//        roomRequestDto.setTag3("테스트3");
//        roomRequestDto.setStudying(false);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("유저네임");
//        //when
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(roomRequestDto, user));
//
//        //then
//        assertThat(e.getMessage()).isEqualTo("방 이름을 입력해주세요.");
//    }
//
//    @Test
//    void 스터디룸_기업분류_미선택() {
//        //given
//        RoomRequestDto roomRequestDto = new RoomRequestDto();
//        roomRequestDto.setTitle("테스트2");
//        roomRequestDto.setRoomId("awd452-3wrf-3wf-fr3-adawd");
//        roomRequestDto.setUserCount(3L);
//        roomRequestDto.setMaxUser(4);
////        roomRequestDto.setTag1("테스트1");
//        roomRequestDto.setTag2("테스트2");
//        roomRequestDto.setTag3("테스트3");
//        roomRequestDto.setStudying(false);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("유저네임");
//        //when
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(roomRequestDto, user));
//
//        //then
//        assertThat(e.getMessage()).isEqualTo("기업분류를 선택해주세요");
//    }
//
//    @Test
//    void 스터디룸_신입경력_미선택() {
//        //given
//        RoomRequestDto roomRequestDto = new RoomRequestDto();
//        roomRequestDto.setTitle("테스트2");
//        roomRequestDto.setRoomId("awd452-3wrf-3wf-fr3-adawd");
//        roomRequestDto.setUserCount(3L);
//        roomRequestDto.setMaxUser(4);
//        roomRequestDto.setTag1("테스트1");
////        roomRequestDto.setTag2("테스트2");
//        roomRequestDto.setTag3("테스트3");
//        roomRequestDto.setStudying(false);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("유저네임");
//        //when
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(roomRequestDto, user));
//
//        //then
//        assertThat(e.getMessage()).isEqualTo("신입/경력을 선택해주세요");
//    }
//
//    @Test
//    void 스터디룸_면접유형_미선택() {
//        //given
//        RoomRequestDto roomRequestDto = new RoomRequestDto();
//        roomRequestDto.setTitle("테스트2");
//        roomRequestDto.setRoomId("awd452-3wrf-3wf-fr3-adawd");
//        roomRequestDto.setUserCount(3L);
//        roomRequestDto.setMaxUser(4);
//        roomRequestDto.setTag1("테스트1");
//        roomRequestDto.setTag2("테스트2");
////        roomRequestDto.setTag3("테스트3");
//        roomRequestDto.setStudying(false);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("유저네임");
//        //when
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(roomRequestDto, user));
//
//        //then
//        assertThat(e.getMessage()).isEqualTo("면접 유형을 선택해주세요");
//    }
//
//    @Test
//    void 스터디룸_인원수_2인미만() {
//        //given
//        RoomRequestDto roomRequestDto = new RoomRequestDto();
//        roomRequestDto.setTitle("테스트2");
//        roomRequestDto.setRoomId("awd452-3wrf-3wf-fr3-adawd");
//        roomRequestDto.setUserCount(3L);
//        roomRequestDto.setMaxUser(1);
//        roomRequestDto.setTag1("테스트1");
//        roomRequestDto.setTag2("테스트2");
//        roomRequestDto.setTag3("테스트3");
//        roomRequestDto.setStudying(false);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("유저네임");
//        //when
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(roomRequestDto, user));
//
//        //then
//        assertThat(e.getMessage()).isEqualTo("인원수를 선택해주세요");
//    }
//
//    @Test
//    void 스터디룸에서_예상질문_조회하기(){
//        //given
//        String username = "김원희";
//
//        //when
//        User user = userRepository.findByUsername(username).get();
//
//        List<UserQuestion> userQuestionList = userQuestionRepository.findAllByUser(user);
//
//        //then
//        assertThat(userQuestionList).isEqualTo(userQuestionList);
//    }
//
//    @Test
//    void 스터디룸에서_예상질문_조회대상_없을때(){
//        //given
//        String username = "유저네임";
//
//        //when
//        userRepository.findByUsername(username);
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.readQuestion(username));
//
//        //then
//        assertThat(e.getMessage()).isEqualTo("해당 유저가 없습니다.");
//    }
//
//    @Test
//    @WithMockUser(username = "테스트이름")
//    void 스터디룸_입장() {
//        //given
//        RoomEnterRequestDto roomEnterRequestDto= RoomEnterRequestDto.builder()
//                .roomId("test-123-t21e-5t4t45t")
//                .build();
//
//        User user = User.builder()
//                .id(1L)
//                .nickname("닉네임")
//                .build();
//        //when
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> roomService.enterRoom(roomEnterRequestDto, userDetails));
//
//        //then
//
//        Room roomEnter = roomRepository.findByRoomId(roomEnterRequestDto.getRoomId());
//        assertThat(roomEnter.getTitle()).isEqualTo(roomEnter.getTitle());
//
//        assertThat(e.getMessage()).isEqualTo("해당 방이 존재하지 않습니다.");
//   }
//
//}