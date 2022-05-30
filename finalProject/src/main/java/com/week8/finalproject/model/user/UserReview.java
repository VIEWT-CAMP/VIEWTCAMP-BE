package com.week8.finalproject.model.user;

import com.week8.finalproject.dto.user.UserReviewDto;
import com.week8.finalproject.model.Timestamped;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Data
public class UserReview extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private String title;

    @Column(length = 2000)
    private String review;


    public UserReview(UserReviewDto userReviewDto, String username){
        this.username = username;
        this.title = userReviewDto.getTitle();
        this.review = userReviewDto.getReview();
    }
    // 수정은 Void 필수
    public void update(UserReviewDto userReviewDto){
        this.review = userReviewDto.getReview();
    }
}

