package com.week8.finalproject.repository.room;

import com.week8.finalproject.model.user.User;
import com.week8.finalproject.model.room.EnterUser;
import com.week8.finalproject.model.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnterUserRepository extends JpaRepository<EnterUser, Long > {

    List<EnterUser> findByRoom(Room room);
    EnterUser findAllByRoom(Room room);

    EnterUser findByRoomAndUser(Room room, User user);
    EnterUser findAllByUser(User user);

    EnterUser findByUser(User user);
    EnterUser findAllByRoomId(String roomId);
}
