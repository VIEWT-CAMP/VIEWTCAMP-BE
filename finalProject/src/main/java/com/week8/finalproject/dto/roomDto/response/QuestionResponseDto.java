package com.week8.finalproject.dto.roomDto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionResponseDto {
    private Long id;
    private String question;
    private LocalDateTime createdAt;

    public QuestionResponseDto(Long id, String question, LocalDateTime createdAt) {
        this.id = id;
        this.question = question;
        this.createdAt = createdAt;
    }
}
