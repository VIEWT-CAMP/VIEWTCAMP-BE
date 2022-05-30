package com.week8.finalproject.config.handler;

import com.week8.finalproject.model.chat.ChatMessage;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.room.Room;
import com.week8.finalproject.repository.room.RoomRepository;
import com.week8.finalproject.repository.user.UserRepository;
import com.week8.finalproject.repository.room.EnterUserRepository;
import com.week8.finalproject.repository.chat.RedisRepository;
import com.week8.finalproject.security.jwt.JwtDecoder;
import com.week8.finalproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final ChatService chatService;
    private final RedisRepository redisRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final EnterUserRepository enterUserRepository;
    private final Long min = 0L;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // websocket 연결시 헤더의 jwt token 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {

            jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization"));

//            log.info("디코더 토큰 = " + accessor.getFirstNativeHeader("Authorization").substring(7));
//            System.out.println("디코더 토큰sub 제외 = " + accessor.getFirstNativeHeader("Authorization"));

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            String sessionId = (String) message.getHeaders().get("simpSessionId");

            redisRepository.setUserEnterInfo(sessionId, roomId);
            //기존 유저카운터 증가
//            redisRepository.plusUserCount(roomId);
//            String name = jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));
            String name = jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization"));
//            String name = jwtDecoder.decodeNickname(accessor.getFirstNativeHeader("Authorization").substring(7));

            redisRepository.setNickname(sessionId, name);
            try {
                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (roomId != null) {
                Room room = roomRepository.findByroomId(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
                room.setUserCount(redisRepository.getUserCount(roomId));
                if (redisRepository.getUserCount(roomId) < 0) {
                    room.setUserCount(min);
                }
//                roomRepository.save(room);
            }

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");

            String roomId = redisRepository.getUserEnterRoomId(sessionId);
            String name = redisRepository.getNickname(sessionId);

            System.out.println("DISCONNECT 실행 됐니?");
            System.out.println("sessionId = " + sessionId);
            System.out.println("sessionId로 뽑은 roomid = " + roomId);
            System.out.println("sessionId로 뽑은 name = " + name);

            if (roomId != null) {
                redisRepository.minusUserCount(roomId);
                System.out.println("DISCONNECT 조건문 들어왔니?");
                try {
                    System.out.println("DISCONNECT try 들어왔니 ?");
                    chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Room room = roomRepository.findByroomId(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다.(DISCONNECT)"));


//                if (roomId != null) {
//                    System.out.println("DISCONNECT 인원수 증가한걸 적용");
//                    room.setUserCount(redisRepository.getUserCount(roomId));
//                    roomRepository.save(room);
//                }


                User user = userRepository.findByNickname(name);
                if (enterUserRepository.findByRoomAndUser(room, user).getRoom().getRoomId().equals(roomId)) {
//                    EnterUser enterUser = enterUserRepository.findByRoomAndUser(room, user);
//                    enterUserRepository.delete(enterUser);
//
//                    log.info("현재 유저 카운트는 몇? = " + room.getUserCount());
//                    if (room.getUserCount() == 0) {
//                        roomRepository.delete(room);
//                        System.out.println("해치웠나?");
//                        log.info("해치웠나?");
//                    }

                    log.info("USERENTER_DELETE {}, {}", name, roomId);
                }

                redisRepository.removeUserEnterInfo(sessionId);
                log.info("DISCONNECTED {}, {}", sessionId, roomId);
            }
        }
        return message;
    }
}
