package com.joy.krianastore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KrianastoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrianastoreApplication.class, args);
	}

//	@Bean
	public CommandLineRunner commandLineRunner(StoreRepository storeRepository,UserRepository userRepository) {
		return args -> {
			Store store=new Store();
			store.setName("Ghosh General Store");
			var savedStore=storeRepository.save(store);

			User user=new User();
			user.setEmail("srinjoygh@gmail.com");
			user.setPassword("12345678");
			user.setRole(Role.READ_WRITE);
			user.setStore(savedStore);

			var savedUser=userRepository.save(user);

			savedStore.addUser(savedUser);
			storeRepository.save(savedStore);


			User newUser=new User();
			newUser.setEmail("srinjoyghh@gmail.com");
			newUser.setPassword("12345678");
			newUser.setRole(Role.READ_ONLY);
			newUser.setStore(savedStore);
			var savedNewUser=userRepository.save(newUser);
			savedStore.addUser(savedNewUser);
			storeRepository.save(savedStore);
		};
	}

}

