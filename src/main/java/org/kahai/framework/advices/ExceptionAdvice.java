package org.kahai.framework.advices;

import java.util.Map;

import org.kahai.framework.errors.HttpError;
import org.kahai.framework.errors.MethodNotAllowed;
import org.kahai.framework.errors.RouteNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(HttpError.class)
    public ResponseEntity<Map<String, Object>> handleHttpErrors(
        HttpError error
    ) {
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerNotFound() {
        RouteNotFound error = new RouteNotFound();
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerMethodNotAllowed() {
        MethodNotAllowed error = new MethodNotAllowed();
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };
};
