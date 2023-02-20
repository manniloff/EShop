package com.amdaris.mentoring.payment.util.exception;

import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
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

    @ExceptionHandler(PayPalRESTException.class)
    public ResponseEntity<?> handlePayPalRESTException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>("paypal_rest_exception", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
