package com.week8.finalproject.exception;

public class UserException extends BaseException{
    private BaseExceptionType exceptionType;

    public UserException(BaseExceptionType exceptionType){
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType(){
        return exceptionType;
    }

}
