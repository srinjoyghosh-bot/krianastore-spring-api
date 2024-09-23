package com.joy.krianastore;

import com.joy.krianastore.domain.models.Store;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KrianastoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(KrianastoreApplication.class, args);
    }
}

