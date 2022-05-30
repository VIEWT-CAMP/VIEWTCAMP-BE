package com.week8.finalproject.exception;

import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {


    ALREADY_EXIST_USERNAME(600,HttpStatus.CONFLICT,"이미 존재하는 아이디입니다"),
    ALREADY_EXIST_NICKNAME(601, HttpStatus.CONFLICT,"이미 존재하는 닉네임입니다."),
    WRONG_PASSWORD(602,HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    NOT_FOUND_MEMBER(603, HttpStatus.NOT_FOUND, "회원 정보가 없습니다."),
    LOGIN_TOKEN_EXPIRE(604, HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    HAS_ENTER_ROOM(605, HttpStatus.BAD_REQUEST, "이미 입장한 방입니다."),
    ENTER_MAX_USER(606, HttpStatus.BAD_REQUEST, "입장인원을 초과하였습니다"),
    BAN_USER_ROOM(607, HttpStatus.CONFLICT, "추방당한 방은 재입장이 불가능합니다."),
    ROOM_STATUS_TRUE(608, HttpStatus.CONFLICT, "스터디가 진행중인 방에는 입장하실 수 없습니다."),
    HAS_FOLLOW_USER(609, HttpStatus.CONFLICT, "이미 팔로우한 유저입니다.");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;


//
    UserExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }


    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }


}
