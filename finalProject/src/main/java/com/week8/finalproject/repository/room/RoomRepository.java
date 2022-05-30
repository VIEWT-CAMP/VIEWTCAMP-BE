package com.week8.finalproject.repository.room;

import com.week8.finalproject.model.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    Optional<Room> findByroomId(String roomId);

    Room findByTitle(String title);

//    Room findByTitleContains(String title);

    Room findByRoomId(String roomId);

    Page<Room> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Room> findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(Pageable pageable, String title);

    Page<Room> findAllByTag1AndTag2AndTag3OrderByCreatedAtDesc(Pageable pageable, String tag1, String tag2, String tag3);

    List<Room> findTop8ByOrderByCreatedAtDesc();

    List<Room> findAllByOrderByCreatedAtDesc();

//    List<Room> findAllByTag1AndTag2AndTag3(String tag1, String tag2, String tag3);
}