package com.dogtorhouse.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class DogtorHouseAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DogtorHouseAppApplication.class, args);
	}

	@PostConstruct
	public void printSecurityConfig() {
	}

}
