package com.week8.finalproject.repository.user;

import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.user.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserQuestionRepository extends JpaRepository<UserQuestion,Long> {

    List<UserQuestion> findAllByUser(User user);


}
