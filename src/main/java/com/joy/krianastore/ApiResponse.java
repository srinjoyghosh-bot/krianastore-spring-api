package com.joy.krianastore;

public record ApiResponse<T>(boolean success, String message, T data) {
}
