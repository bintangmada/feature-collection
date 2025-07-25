package com.bintang.email_confirmation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailConfirmationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailConfirmationApplication.class, args);
		System.out.println("\nSERVER EMAIL CONFIRMATION IS RUNNING");
	}

}
