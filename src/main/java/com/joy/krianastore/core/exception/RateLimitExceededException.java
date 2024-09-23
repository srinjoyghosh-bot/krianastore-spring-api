package com.joy.krianastore.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Exception thrown when rate limit is exceeded for an endpoint.
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceededException extends RuntimeException{
    /**
     * Constructs a new RateLimitExceededException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public RateLimitExceededException(String message) {
        super(message);
    }
}
