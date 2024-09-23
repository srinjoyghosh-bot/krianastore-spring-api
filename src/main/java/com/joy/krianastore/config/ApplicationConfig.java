package com.joy.krianastore.config;

import com.joy.krianastore.data.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up application-wide beans.
 * This class configures beans needed for authentication and password encoding.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Configures a {@link UserDetailsService} bean for retrieving user details based on user ID.
     *
     * @return a UserDetailsService that fetches user details using {@link UserRepository}
     * @throws UsernameNotFoundException if no user is found with the given ID
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userId -> userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    /**
     * Configures an {@link AuthenticationProvider} bean to handle authentication logic.
     * Uses {@link DaoAuthenticationProvider} to manage authentication using a user details service and a password encoder.
     *
     * @return a configured AuthenticationProvider instance for managing authentication
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Provides an {@link AuthenticationManager} bean capable of processing authentication requests.
     *
     * @param config the {@link AuthenticationConfiguration} used to retrieve the {@link AuthenticationManager}
     * @return an AuthenticationManager instance
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures a {@link PasswordEncoder} bean for encoding passwords.
     * Uses {@link BCryptPasswordEncoder} to securely hash passwords.
     *
     * @return a PasswordEncoder instance that uses BCrypt for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

