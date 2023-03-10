package com.amdaris.mentoring.common.util.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.lang.model.UnknownEntityException;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnknownEntityException.class)
    public ResponseEntity<?> handleEntityNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>("entity_not_found", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>("no_such_element", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>("argument_type_mismatch", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
