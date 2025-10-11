package com.eg.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"com.eg.user", "com.eg.common"})
@EnableCaching
public class UserServiceApplication {

	public static void main(String[] args) {
		System.out.println("Running with Java Version: " + System.getProperty("java.version"));
		System.out.println("Render password: " + System.getenv("DB_PASSWORD"));
		System.out.println("Render wallet path: " + System.getenv("WALLET_PATH"));
		System.out.println("Render db name: " + System.getenv("ORACLE_DB_NAME"));
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
