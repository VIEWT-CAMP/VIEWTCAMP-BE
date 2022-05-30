package com.week8.finalproject.repository.user;

import com.week8.finalproject.model.user.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReview,Long> {
    List<UserReview> findAllByUsernameOrderByCreatedAtDesc(String username);
}
