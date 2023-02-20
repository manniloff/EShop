package com.amdaris.mentoring.core.util.exception;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException(String errorMessage) {
        super(errorMessage);
    }
}
