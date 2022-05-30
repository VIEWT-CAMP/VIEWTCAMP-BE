package com.week8.finalproject.service;

import com.week8.finalproject.dto.roomDto.request.RoomEnterRequestDto;
import com.week8.finalproject.dto.roomDto.request.RoomRequestDto;
import com.week8.finalproject.dto.roomDto.response.EnterUserResponseDto;
import com.week8.finalproject.dto.roomDto.response.RoomResponseDto;
import com.week8.finalproject.exception.UserException;
import com.week8.finalproject.exception.UserExceptionType;
import com.week8.finalproject.model.room.BanUser;
import com.week8.finalproject.model.room.EnterUser;
import com.week8.finalproject.model.room.Room;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.user.UserQuestion;
import com.week8.finalproject.repository.room.RoomRepository;
import com.week8.finalproject.repository.room.BanUserRepository;
import com.week8.finalproject.repository.room.EnterUserRepository;
import com.week8.finalproject.repository.room.RoomSpecification;
import com.week8.finalproject.repository.user.UserQuestionRepository;
import com.week8.finalproject.repository.user.UserRepository;
import com.week8.finalproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final EnterUserRepository enterUserRepository;
    private final UserRepository userRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final BanUserRepository banUserRepository;
    //방 생성
    public RoomResponseDto createRoom(RoomRequestDto requestDto, User user){

        if (roomRepository.findByTitle(requestDto.getTitle()) != null) {
            throw new IllegalArgumentException("이미 존재하는 방 이름입니다.");
        }

        if (requestDto.getTitle() == null || requestDto.getTitle().equals(" ")) {
            throw new IllegalArgumentException("방 이름을 입력해주세요.");
        }

        if (requestDto.getTag1() == null) {
            throw new IllegalArgumentException("기업분류를 선택해주세요");
        } else if (requestDto.getTag2() == null) {
            throw new IllegalArgumentException("신입/경력을 선택해주세요");
        } else if (requestDto.getTag3() == null) {
            throw new IllegalArgumentException("면접 유형을 선택해주세요");
        }

        int maxUser = requestDto.getMaxUser();
        if(maxUser < 2){
            throw new IllegalArgumentException("인원수를 선택해주세요");
        }

        Room room = Room.create(requestDto, user, maxUser);
        Room createRoom = roomRepository.save(room);
        String title = createRoom.getTitle();
        String roomId = createRoom.getRoomId();
        Long userCount = 0L;
        String tag1 = createRoom.getTag1();
        String tag2 = createRoom.getTag2();
        String tag3 = createRoom.getTag3();
        LocalDateTime createAt = createRoom.getCreatedAt();

        return new RoomResponseDto(title, roomId, userCount, maxUser, tag1, tag2, tag3, createAt, user);
    }

    //방 진입
    public List<EnterUserResponseDto> enterRoom(RoomEnterRequestDto roomEnterRequestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        String roomId = roomEnterRequestDto.getRoomId();
        // 받은 룸id로 내가 입장할 받을 찾음
        Room room = roomRepository.findByroomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));

        // 들어갈 방에서 유저를 찾음
        EnterUser enterCheck = enterUserRepository.findByRoomAndUser(room, user);

        // 내가 입장할 방이 추방당한 방인지 확인
        BanUser banUserCheck = banUserRepository.findByRoomAndUser(room, user);
        if (banUserCheck != null) {
            throw new UserException(UserExceptionType.BAN_USER_ROOM);
        }

        boolean roomStatusCheck = room.isStudying();

        // 스터디가 진행중이라면 입장 불가.
        if (roomStatusCheck == true) {
            throw new UserException(UserExceptionType.ROOM_STATUS_TRUE);
        }

        // 해당 방에 입장 되어있는 경우 (이미 입장한 방인경우)
        if (enterCheck != null) {
            throw new UserException(UserExceptionType.HAS_ENTER_ROOM);
        }
        //들어갈 방을 찾음? (들어온 유저가 몇명인지 체크 ?) (동일한 룸이 몇개인지 확인하여 리스트에 담아 몇명인지를 확인)
        List<EnterUser> enterUserSize = enterUserRepository.findByRoom(room);

        //방을 입장할때마다 몇명이 있는지 확인하는 로직(입장인원 초과 확인)
        int maxUser = room.getMaxUser();
        if (enterUserSize.size() > 0) {
            if (maxUser < enterUserSize.size() + 1) {
                throw new UserException(UserExceptionType.ENTER_MAX_USER);
            }
        }

        // 나가기 처리가 되지않아 내가 아직 특정방에 남아있는상태라면
        EnterUser enterUserCheck = enterUserRepository.findAllByUser(user);
        if (enterUserCheck != null) {
            enterUserRepository.delete(enterUserCheck);
        }

        Long userCount = room.getUserCount() + 1;
        //유저카운터 증가
        room.setUserCount(userCount);
        roomRepository.save(room);

        //방에 입장시 유저 한명이되는꼴
        EnterUser enterUser = new EnterUser(user, room);
        enterUserRepository.save(enterUser);

        // 방에 입장한 사람들을 리스트에 담음
        List<EnterUser> enterUsers = enterUserRepository.findByRoom(room);
        List<EnterUserResponseDto> enterRoomUsers = new ArrayList<>();
        for (EnterUser enterUser2 : enterUsers) {
            enterRoomUsers.add(new EnterUserResponseDto(
                    //방에 입장한 유저의 이름
                    enterUser2.getUser().getNickname(),
                    //방에 입장한 유저의 프로필
                    enterUser2.getUser().getProfileImg()
            ));
        }
        return enterRoomUsers;
    }

    //방 나가기
    @Transactional
    public void quitRoom(String roomId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        //내가입장한 방을 찾음
        Room room = roomRepository.findByroomId(roomId).orElseThrow(()-> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
        //방에서 내가 입장했던 스터디룸을 찾은다음
        EnterUser enterUser =  enterUserRepository.findByRoomAndUser(room, user);
        // 그 기록을 지워서 내가 들어가있던 상태를 나간 상태로 만든다.
        enterUserRepository.delete(enterUser);
        //내가 방을 나갔으니, room의 유저 카운터를 -1 해준다
        Long userCount = room.getUserCount() - 1;
        //유저카운터 감소
        room.setUserCount(userCount);
        roomRepository.save(room);

        //userCount 체크하여 0이면 다 나간것으로 간주하여 방 폭파
        if (room.getUserCount() == 0) {
            List<BanUser> banUser = banUserRepository.findAllByRoom(room);
            if (banUser != null) {
                banUserRepository.deleteAll(banUser);
            }
            roomRepository.delete(room);
        }
    }
    // 스터디 목록 페이지 전체 화상 채팅방 조회
    public List<Room> allReadRoom() {
        return roomRepository.findAllByOrderByCreatedAtDesc();
    }

    //메인페이지 상위 8개 화상 채팅방 조회
    @Transactional
    public List<Room> mainPageReadRoom() {
        return roomRepository.findTop8ByOrderByCreatedAtDesc();
    }

    // 스터디 목록 페이징처리
    @Transactional
    public Page<Room> getPageRoom(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return roomRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 스터디목록 페이지 조회
    @Transactional
    public Page<Room> getTagRoom(int page, int size, String sortBy, String recruit, String tag1, String tag2, String tag3, String keyword) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Room> spec = (root, query, criteriaBuilder) -> null;
        if(recruit.equals("recruiting"))
            spec = spec.and(RoomSpecification.equalStudying(false));
        if (!tag1.equals("null"))
            spec = spec.and(RoomSpecification.equalTag1(tag1));
        if (!tag2.equals("null"))
            spec = spec.and(RoomSpecification.equalTag2(tag2));
        if (!tag3.equals("null"))
            spec = spec.and(RoomSpecification.equalTag3(tag3));
        if (!keyword.equals("null"))
            spec = spec.and(RoomSpecification.equalTitle(keyword));
        return roomRepository.findAll(spec, pageable);
    }

    // 스터디룸에서 각 유저들예상질문 조회
    @Transactional
    public List<UserQuestion> readQuestion(String username) {
        // 해당 유저의 이름으로 유저객체를 찾기
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        // 위에서 찾은 유저객체로 userQuestion 중에 해당 User의 question를 찾기
        return userQuestionRepository.findAllByUser(user);
    }

    // 스터디룸의 입장 유저정보 조회
    public List<EnterUser> enterUsers(String roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        return enterUserRepository.findByRoom(room);
    }

    // 스터디룸 검색 기능
    public Page<Room> roomSearch(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        return roomRepository.findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(pageable, keyword);
    }
}
