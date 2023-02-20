package com.amdaris.mentoring.payment.util.exception;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException(String errorMessage) {
        super(errorMessage);
    }
}
