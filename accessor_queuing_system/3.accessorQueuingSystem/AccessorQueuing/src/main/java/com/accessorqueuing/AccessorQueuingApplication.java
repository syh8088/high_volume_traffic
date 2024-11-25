package com.accessorqueuing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AccessorQueuingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessorQueuingApplication.class, args);
	}

}
