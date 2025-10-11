package com.eg.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"com.eg.user", "com.eg.common"})
@EnableCaching
public class UserServiceApplication {

	public static void main(String[] args) {
		System.out.println("Running with Java Version: " + System.getProperty("java.version"));
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
