package com.joy.krianastore.domain.dto;

/**
 * General DTO for all API responses
 * @param success indicator if API request was successful or not
 * @param message message with the request
 * @param data data returned, if any, from the request.
 * @param <T> Type of the data returned
 */
public record ApiResponse<T>(boolean success, String message, T data) {
}
