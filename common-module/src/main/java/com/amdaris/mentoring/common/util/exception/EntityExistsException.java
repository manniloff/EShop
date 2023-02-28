package com.amdaris.mentoring.common.util.exception;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException(String errorMessage) {
        super(errorMessage);
    }
}
