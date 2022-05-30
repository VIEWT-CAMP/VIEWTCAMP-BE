package com.week8.finalproject.repository.room;

import com.week8.finalproject.model.room.BanUser;
import com.week8.finalproject.model.room.EnterUser;
import com.week8.finalproject.model.room.Room;
import com.week8.finalproject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BanUserRepository extends JpaRepository<BanUser, Long > {

    //    List<EnterUser> findByRoom(Room room);
    List<BanUser> findAllByRoom(Room room);
//    List
    BanUser findByRoomAndUser(Room room, User user);
}
