package com.joy.krianastore.core.exception;

import com.joy.krianastore.domain.dto.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ApiResponse<>(false, errorMessage, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, "Resource not found-" + ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleRateLimitExceededException(RateLimitExceededException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, "Rate limit exceeded", ex.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, "Internal Server Error-" + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
