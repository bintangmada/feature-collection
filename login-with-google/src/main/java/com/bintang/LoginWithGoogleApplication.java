package com.bintang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginWithGoogleApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginWithGoogleApplication.class, args);
		System.out.println("\nSERVER IS RUNNING");
	}

}
