package com.joy.krianastore.core.exception;
/**
 * Exception thrown when a currency conversion operation fails.
 * This exception signals issues such as unsupported currencies or
 * failures in accessing the currency conversion service.
 */
public class CurrencyConversionException extends RuntimeException{
    /**
     * Constructs a new CurrencyConversionException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public CurrencyConversionException(String message) {
        super(message);
    }
}
