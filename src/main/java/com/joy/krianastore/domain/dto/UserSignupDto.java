package com.joy.krianastore.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for transferring user data between the client and server while signing up new user.
 * @param email email id of new user
 * @param password password of new user
 * @param storeName name of the store of the user
 */
public record UserSignupDto(@Email(message = "Provide a valid email") @NotNull(message = "Email cannot be null") String email,
                            @NotNull(message = "Password cannot be null") @Size(min = 5, max = 8, message = "Password must be 5 to 8 characters long") String password,@NotNull(message = "Store name cannot be null") @NotEmpty(message = "Store name cannot be empty") String storeName) {
}
