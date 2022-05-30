package com.week8.finalproject.controller;

import com.week8.finalproject.dto.roomDto.request.RoomEnterRequestDto;
import com.week8.finalproject.dto.roomDto.request.RoomRequestDto;
import com.week8.finalproject.dto.roomDto.response.EnterUserResponseDto;
import com.week8.finalproject.dto.roomDto.response.RoomResponseDto;
import com.week8.finalproject.model.room.EnterUser;
import com.week8.finalproject.model.room.Room;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.user.UserQuestion;
import com.week8.finalproject.security.UserDetailsImpl;
import com.week8.finalproject.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    //방 생성
    @PostMapping("/room")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(roomService.createRoom(requestDto, user));
    }

    // 카테고리 방 조회
//    @GetMapping("/room/{tag1}/{tag2}/{tag3}")
//    public ResponseEntity<List<Room>> readRoom(
//            @PathVariable String tag1,
//            @PathVariable String tag2,
//            @PathVariable String tag3
//            ) {
//        return ResponseEntity.ok().body(roomService.readRoom(tag1, tag2, tag3));
//    }
//
    // 스터디룸 목록 전체 조회
    @GetMapping("/room/all")
    public List<Room> allReadRoom() {
        return roomService.allReadRoom();
    }

    // 최신순 스터디룸 TOP8  조회
    @GetMapping("/room")
    public List<Room> mainPageReadRoom() {
        return roomService.mainPageReadRoom();
    }

    //  방 조회 페이지 처리
    @GetMapping("/room-page/{page}/{size}")
    @ResponseBody
    public Page<Room> roomscrooll(@PathVariable int page,
                                  @PathVariable int size
    ) {
        page = page - 1;
        return roomService.getPageRoom(page, size);
    }

    // 스터디목록 페이지 스터디룸 조회
    @GetMapping("/room-page/{page}/{size}/{sortBy}/{recruit}/{tag1}/{tag2}/{tag3}/{keyword}")
    @ResponseBody
    public Page<Room> getTagRoom(@PathVariable int page,
                                 @PathVariable int size,
                                 @PathVariable String sortBy,
                                 @PathVariable String recruit,
                                 @PathVariable(required = false) String tag1,
                                 @PathVariable(required = false) String tag2,
                                 @PathVariable(required = false) String tag3,
                                 @PathVariable(required = false) String keyword
    ) {
        page = page - 1;
        return roomService.getTagRoom(page, size, sortBy, recruit, tag1 ,tag2 ,tag3, keyword);
    }

    // 스터디룸 안에서 유저 예상질문 조회
    @GetMapping("/room/question/{username}")
    public List<UserQuestion> readQuestion(@PathVariable String username) {
        return roomService.readQuestion(username);
    }

    // 스터디룸에 입장  userEnter 테이블 조인(현재 방에 접속 중인 유저 확인 테이블)
    @PostMapping("/user-enter")
    public ResponseEntity<List<EnterUserResponseDto>> enterRoom(@RequestBody RoomEnterRequestDto roomEnterRequestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(roomService.enterRoom(roomEnterRequestDto, userDetails));
    }

    // 스터디룸에 입장한 유저들 정보 조회
    @GetMapping("/user-enter/{roomId}")
    public List<EnterUser> enterUsers(@PathVariable String roomId) {
        return roomService.enterUsers(roomId);
    }

    // 스터디룸에서 퇴장 enterUser 삭제
    @DeleteMapping("/user-quit/{roomId}")
    public void quitRoom(@PathVariable String roomId,
                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        roomService.quitRoom(roomId, userDetails);
    }

    // 스터디룸 검색 기능
    @GetMapping("/room-page/{page}/{size}/{keyword}")
    @ResponseBody
    public Page<Room> roomSearch(@PathVariable int page,
                                  @PathVariable int size,
                                  @PathVariable String keyword
    ) {
        page = page - 1;
        return roomService.roomSearch(page, size, keyword);
    }
}
