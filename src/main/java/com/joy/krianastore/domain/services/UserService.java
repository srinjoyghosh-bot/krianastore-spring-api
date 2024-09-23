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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void userSignUp(UserSignupDto userSignupDto) {
        Store store = new Store();
        store.setName(userSignupDto.storeName());
        var savedStore=storeRepository.save(store);
        User user = new User();
        user.setEmail(userSignupDto.email());
        user.setPassword(passwordEncoder.encode(userSignupDto.password()));
        user.setStore(savedStore);
        user.setRole(Role.ADMIN);
        var savedUser=userRepository.save(user);
        savedStore.addUser(savedUser);
        storeRepository.save(savedStore);
    }

    public void createUser(UserCreateDto dto, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var optionalStore=storeRepository.findById(user.getStore().getId());
        if(optionalStore.isEmpty()) {
            throw new ResourceNotFoundException("Store not found with id: " + user.getStore().getId());
        }
        var store=optionalStore.get();
        User newUser=new User();
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRole(dto.role());
        newUser.setStore(store);
        userRepository.save(newUser);
        store.addUser(newUser);
        storeRepository.save(store);
    }

    public UserLoginResponseDto loginUser(UserLoginDto dto) {
        var optionalUser=userRepository.findByEmail(dto.email());
        if(optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with email: " + dto.email());
        }
        var user=optionalUser.get();
        if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
        var token=jwtService.generateToken(user);
        return new UserLoginResponseDto(token);

    }
}
