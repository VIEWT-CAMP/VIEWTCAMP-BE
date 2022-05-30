package com.week8.finalproject.dto.chat;

import com.week8.finalproject.model.chat.ChatMessage;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {
    private ChatMessage.MessageType type;
    private Long roomId;
    private String sender;
    private String message;
    private long userCount;
    private String profileImg;
}
