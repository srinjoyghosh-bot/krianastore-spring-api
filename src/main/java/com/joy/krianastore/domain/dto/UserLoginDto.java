package com.joy.krianastore.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for transferring user data between the client and server while logging in user.
 * @param email email id of new user
 * @param password password of new user
 */
public record UserLoginDto(@Email @NotNull String email, @NotNull @Size(min = 5,max = 8,message = "Password must be 5 to 8 characters long") String password) {
}
