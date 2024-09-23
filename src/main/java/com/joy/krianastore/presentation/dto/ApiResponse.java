package com.joy.krianastore.presentation.dto;

public record ApiResponse<T>(boolean success, String message, T data) {
}
