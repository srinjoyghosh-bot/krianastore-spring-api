package com.joy.krianastore.domain.dto;

public record ApiResponse<T>(boolean success, String message, T data) {
}
