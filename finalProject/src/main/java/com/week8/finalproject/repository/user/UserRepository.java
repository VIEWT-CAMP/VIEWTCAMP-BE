package com.week8.finalproject.repository.user;

import com.week8.finalproject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    User findAllByUsername(String username);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByNaverId(String naverId);

    User findByNickname(String sender);

    User findALlByKakaoId(Long kakaoId);

}

