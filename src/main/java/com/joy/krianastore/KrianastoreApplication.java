package com.joy.krianastore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Entry point for the Kirana store Spring Boot application.
 * This class contains the main method that bootstraps the application.
 */
@SpringBootApplication
@EnableCaching
public class KrianastoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(KrianastoreApplication.class, args);
    }
}

