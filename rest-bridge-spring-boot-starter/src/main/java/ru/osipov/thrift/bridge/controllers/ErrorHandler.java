package ru.osipov.thrift.bridge.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.osipov.thrift.bridge.domain.exception.NotFoundException;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity processEntityNotFound(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity processMissingRequestHeader(MissingRequestHeaderException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
}
