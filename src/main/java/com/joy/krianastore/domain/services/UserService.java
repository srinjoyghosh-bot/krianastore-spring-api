package com.joy.krianastore.domain.services;

import com.joy.krianastore.core.exception.ResourceNotFoundException;
import com.joy.krianastore.data.dao.StoreRepository;
import com.joy.krianastore.data.dao.UserRepository;
import com.joy.krianastore.data.models.Role;
import com.joy.krianastore.data.models.Store;
import com.joy.krianastore.data.models.User;
import com.joy.krianastore.domain.dto.UserCreateDto;
import com.joy.krianastore.domain.dto.UserLoginDto;
import com.joy.krianastore.domain.dto.UserLoginResponseDto;
import com.joy.krianastore.domain.dto.UserSignupDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Creates a new user and a new store of that user
     * @param userSignupDto is the user and store details
     */
    public void userSignUp(UserSignupDto userSignupDto) {
        log.info("Registering user {}", userSignupDto);
        Store store = new Store();
        store.setName(userSignupDto.storeName());
        var savedStore = storeRepository.save(store);
        User user = new User();
        user.setEmail(userSignupDto.email());
        user.setPassword(passwordEncoder.encode(userSignupDto.password()));
        user.setStore(savedStore);
        user.setRole(Role.ADMIN);
        log.info("Saving user {}", user);
        var savedUser = userRepository.save(user);
        savedStore.addUser(savedUser);
        log.info("Saving store {}", savedStore);
        storeRepository.save(savedStore);
    }

    /**
     * New user being created by an existing user with ADMIN role
     * @param dto is the details of the new user
     * @param connectedUser is the details of the currently logged-in user
     * @throws ResourceNotFoundException if store of that user is not found
     */
    public void createUser(UserCreateDto dto, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var optionalStore = storeRepository.findById(user.getStore().getId());
        if (optionalStore.isEmpty()) {
            log.error("Store {} not found", user.getStore().getId());
            throw new ResourceNotFoundException("Store not found with id: " + user.getStore().getId());
        }
        var store = optionalStore.get();
        User newUser = new User();
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRole(dto.role());
        newUser.setStore(store);
        log.info("Saving new user {}", newUser);
        userRepository.save(newUser);
        store.addUser(newUser);
        log.info("Updating store {}", store);
        storeRepository.save(store);
    }

    /**
     * Logs in a user
     * @param dto is the user details
     * @return the token for the logged in user
     * @throws ResourceNotFoundException is user is not found for the email
     * @throws ResponseStatusException is password is wrong
     */
    public UserLoginResponseDto loginUser(UserLoginDto dto) {
        var optionalUser = userRepository.findByEmail(dto.email());
        if (optionalUser.isEmpty()) {
            log.error("User {} not found", dto.email());
            throw new ResourceNotFoundException("User not found with email: " + dto.email());
        }
        var user = optionalUser.get();
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            log.error("Wrong password");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }
        //TODO
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
        log.info("Generating token for user {}", user);
        var token = jwtService.generateToken(user);
        log.info("Logged in user {}", user);
        return new UserLoginResponseDto(token);

    }
}
