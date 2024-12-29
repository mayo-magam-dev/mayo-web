package com.example.mayoweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MayowebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MayowebApplication.class, args);
	}

}
