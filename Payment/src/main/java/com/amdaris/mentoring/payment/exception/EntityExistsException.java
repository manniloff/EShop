package com.amdaris.mentoring.payment.exception;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException(String errorMessage) {
        super(errorMessage);
    }
}
