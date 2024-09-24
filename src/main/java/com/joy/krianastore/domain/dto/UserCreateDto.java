package com.joy.krianastore.domain.dto;

import com.joy.krianastore.data.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for transferring user data between the client and server while creating new user.
 * @param email email id of new user
 * @param password password of new user
 * @param role role of new user. it is ADMIN or READ_WRITE or READ_ONLY
 */
public record UserCreateDto(@Email(message = "Provide a valid email") @NotNull(message = "Email cannot be null") String email, @NotNull(message = "Password cannot be null") @Size(min = 5,max = 8,message = "Password must be 5 to 8 characters long") String password,@NotNull(message = "Roles must be ADMIN or READ_WRITE or READ_ONLY") Role role) {
}
