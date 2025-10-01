package com.eg.ride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(scanBasePackages = {"com.eg.ride", "com.eg.common"})
public class RideApplication {
  static {
    // This ensures the SecurityContext is inherited by child threads
    // created within the same execution
    SecurityContextHolder.setStrategyName(
      SecurityContextHolder.MODE_INHERITABLETHREADLOCAL
    );
  }

	public static void main(String[] args) {


    SpringApplication.run(RideApplication.class, args);
	}

}
