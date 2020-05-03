package io.github.artemy.osipov.thrift.bridge.controllers;

import io.github.artemy.osipov.thrift.bridge.domain.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = BridgeController.class)
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> processEntityNotFound(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> processMissingRequestHeader(MissingRequestHeaderException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
}
