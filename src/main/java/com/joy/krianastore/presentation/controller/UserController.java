package com.joy.krianastore.presentation.controller;

import com.joy.krianastore.domain.dto.*;
import com.joy.krianastore.domain.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * REST Controller to manage users
 * Provides endpoints to create,sign up and log in users
 */
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Sign up new user and create store for that user
     * @param userSignupDto contains the user and store details
     * @return the details of the new user and store
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserSignupDto>> signUp(@Valid @RequestBody UserSignupDto userSignupDto) {
        userService.userSignUp(userSignupDto);
        return new ResponseEntity<>(new ApiResponse<>(true,"User signed up!",userSignupDto), HttpStatus.CREATED);
    }

    /**
     * User of an existing store created by an existing ADMIN user
     * @param userCreateDto contains the details of the new user
     * @param connectedUser is the existing logged-in user
     * @return the details of the new user
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserCreateDto>> create(@Valid @RequestBody UserCreateDto userCreateDto, Principal connectedUser) {
        userService.createUser(userCreateDto, connectedUser);
        return new ResponseEntity<>(new ApiResponse<>(true,"User Created!",userCreateDto), HttpStatus.CREATED);
    }

    /**
     * Log in an existing user
     * @param userLoginDto contains the user credentials
     * @return the JWT token of the logged-in user
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDto>> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        var response=userService.loginUser(userLoginDto);
        return new ResponseEntity<>(new ApiResponse<>(true,"User logged in!",response),HttpStatus.OK);
    }
}
