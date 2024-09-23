package com.joy.krianastore.domain.dto;

/**
 * Data Transfer Object for transferring user token data between the client and server.
 * @param token token of logged in user
 */
public record UserLoginResponseDto(String token) {
}
