package com.recipify.recipify.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<CustomException> handleRuntimeException(RuntimeException exception) {
        CustomException customException = new CustomException(
                500,
                Set.of(exception.getMessage()),
                Timestamp.from(Instant.now())
        );
        exception.printStackTrace();
        log.error("Internal server exception occurred: {}", exception.getMessage());
        return new ResponseEntity<>(customException, HttpStatusCode.valueOf(500));
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<CustomException> handleValidationException(ValidationException exception) {
        CustomException customException = new CustomException(
                400,
                Set.of(exception.getMessage()),
                Timestamp.from(Instant.now())
        );
        exception.printStackTrace();
        log.error("Validation exception occurred: {}", exception.getMessage());
        return new ResponseEntity<>(customException, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(IntegrationException.class)
    protected ResponseEntity<CustomException> handleIntegrationException(IntegrationException exception) {
        CustomException customException = new CustomException(
                exception.getStatus().value(),
                Set.of(exception.getMessage()),
                Timestamp.from(Instant.now())
        );
        exception.printStackTrace();
        log.error("Integration exception occurred: {}", exception.getMessage());
        return new ResponseEntity<>(customException, exception.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Set<String> messages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());

        CustomException customException = new CustomException(
                status.value(),
                messages,
                Timestamp.from(Instant.now())
        );
        exception.printStackTrace();
        log.error("Validation error occurred {}", exception.getMessage());
        return new ResponseEntity<>(customException, status);
    }

}
