package io.github.artemy.osipov.thrift.bridge.controllers;

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = BridgeController.class)
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> processEntityNotFound(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
