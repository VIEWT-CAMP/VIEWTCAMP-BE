package com.week8.finalproject.controller;

import com.week8.finalproject.model.chat.ChatMessage;
import com.week8.finalproject.repository.chat.RedisRepository;
import com.week8.finalproject.security.jwt.JwtDecoder;
import com.week8.finalproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisRepository redisRepository;
    private final ChatService chatService;
    //토근 관련 문제있을시 임포트  oauth로 해볼것
    private final JwtDecoder jwtDecoder;


    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("Authorization") String token)
            throws InterruptedException {
        //토큰 오류 있을경우 확인하기
//        token = token.substring(7);
//        System.out.println("들어온 토큰 = " + token);
//        log.info("들어온 토큰 = " + token);
        message.setSender(jwtDecoder.decodeUsername(token));
        message.setProfileImg(jwtDecoder.decodeprofileImg(token));
        message.setUserCount(redisRepository.getUserCount(message.getRoomId()));
//        chatMessageRepository.save(message);
        chatService.sendChatMessage(message);

    }

}