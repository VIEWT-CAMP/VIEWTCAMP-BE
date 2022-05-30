package com.week8.finalproject.model.user;

import com.week8.finalproject.dto.roomDto.request.QuestionRequestDto;
import com.week8.finalproject.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestion extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String question;


    public static UserQuestion userQuestion(QuestionRequestDto questionRequestDto, User user) {
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.user = user;
        userQuestion.question = questionRequestDto.getQuestion();
        return userQuestion;
    }

}
