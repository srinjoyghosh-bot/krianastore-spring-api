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

/**
 * Centralized exception handler for the application. It handles exceptions thrown
 * by controllers and presentation layers, returning appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles MethodArgumentNotValidException and returns an appropriate HTTP response
     * with a custom error message and status code.
     *
     * @param ex the MethodArgumentNotValidException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ApiResponse<>(false, errorMessage, null), HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles ResourceNotFoundException and returns an appropriate HTTP response
     * with a custom error message and status code.
     *
     * @param ex the ResourceNotFoundException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, "Resource not found-" + ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }
    /**
     * Handles RateLimitExceededException and returns an appropriate HTTP response
     * with a custom error message and status code.
     *
     * @param ex the RateLimitExceededException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleRateLimitExceededException(RateLimitExceededException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, "Rate limit exceeded", ex.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }
    /**
     * Handles ResponseStatus and returns an appropriate HTTP response
     * with the error message and status code.
     *
     * @param ex the ResponseStatusException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), ex.getStatusCode());
    }
    /**
     * Handles CurrencyConversionException and returns an appropriate HTTP response
     * with the error message and status code.
     *
     * @param ex the CurrencyConversionException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(CurrencyConversionException.class)
    public ResponseEntity<ApiResponse<String>> handleCurrencyConversionException(CurrencyConversionException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles generic exceptions that occur within the application.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, "Internal Server Error-" + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
