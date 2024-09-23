package com.joy.krianastore.domain.services;

import com.joy.krianastore.data.StoreRepository;
import com.joy.krianastore.data.UserRepository;
import com.joy.krianastore.domain.models.Role;
import com.joy.krianastore.domain.models.Store;
import com.joy.krianastore.domain.models.User;
import com.joy.krianastore.presentation.dto.UserCreateDto;
import com.joy.krianastore.presentation.dto.UserLoginDto;
import com.joy.krianastore.presentation.dto.UserLoginResponseDto;
import com.joy.krianastore.presentation.dto.UserSignupDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            //TODO throw exception
            return;
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
            //TODO throw exception
            return null;
        }
        var user=optionalUser.get();
        if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
            //TODO throw exception
            return null;
        }
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
        var token=jwtService.generateToken(user);
        return new UserLoginResponseDto(token);

    }
}
