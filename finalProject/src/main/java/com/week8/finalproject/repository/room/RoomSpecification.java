package com.week8.finalproject.repository.room;

import com.week8.finalproject.model.room.Room;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RoomSpecification {

    public static Specification<Room> equalTag1(String tag1) {
        return new Specification<Room>() {
            @Override
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("tag1"), tag1);
            }
        };
    }

    public static Specification<Room> equalTag2(String tag2) {
        return new Specification<Room>() {
            @Override
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("tag2"), tag2);
            }
        };
    }

    public static Specification<Room> equalTag3(String tag3) {
        return new Specification<Room>() {
            @Override
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("tag3"), tag3);
            }
        };
    }

    public static Specification<Room> equalTitle(String keyword) {
        return new Specification<Room>() {
            @Override
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("title"),"%" + keyword + "%");
            }
        };
    }

    public static Specification<Room> equalStudying(boolean recruit) {
        return new Specification<Room>() {
            @Override
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("studying"),recruit);
            }
        };
    }
}

