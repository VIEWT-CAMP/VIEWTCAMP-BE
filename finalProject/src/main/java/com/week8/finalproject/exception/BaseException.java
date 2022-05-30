package com.week8.finalproject.exception;


public abstract class BaseException extends RuntimeException {
    public abstract BaseExceptionType getExceptionType();
    //BaseException은 앞으로 정의할 모든 커스텀 예외의 부모 클래스로,
    // 앞으로 생성할 커스텀 예외 클래스들을 BaseException
    // 타입으로 처리할 수 있도록 하기 위해서 만들어주었습니다.
}
//
