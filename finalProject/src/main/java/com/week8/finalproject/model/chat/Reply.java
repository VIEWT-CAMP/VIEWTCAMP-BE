package com.week8.finalproject.model.chat;

import com.week8.finalproject.model.Timestamped;
import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.user.UserQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reply extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String comments;

    @ManyToOne
    @JoinColumn(name = "contents_id")
    private UserQuestion contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
