package com.week8.finalproject.repository.user;

import com.week8.finalproject.model.user.Following;
import com.week8.finalproject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowingRepositoy extends JpaRepository<Following,Long> {
    List<Following> findAllByUser(User user);
    Following findByUser(User user);
//    List<Following> findAllByFollowingUser(User user);
    Following findByFollowingUser(User followingUser);
    Following findAllById(Long id);

    List<Following> findAllByFollowingUser(User followingUser);

    // select = 필드(컬럼), FROM = 테이블(ENTITY)
//    @Query(value = "select user, followingUser from Following group by user, followingUser having user")
    Following findByUserAndFollowingUser(User user,User followingUser);
    Following findAllByUserAndFollowingUser(Long user,Long followingUser);

//    @Query("select User FROM Following WHERE Following.followingUser =?1 and Following.user =?2")
    Following findByFollowingUserAndUser(User followingUser, User user);


}
