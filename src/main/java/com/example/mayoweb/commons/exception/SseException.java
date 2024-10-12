package com.example.mayoweb.commons.exception;

import com.example.mayoweb.commons.exception.payload.ErrorStatus;

public class SseException extends ApplicationException{

    public SseException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}

