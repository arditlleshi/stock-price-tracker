package com.stockpricetracker.stockpricetracker.exceptions;

public class ApiLimitExceededException extends RuntimeException {
    public ApiLimitExceededException(String message){
        super(message);
    }
}
