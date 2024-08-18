package com.example.mayoweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableAsync
public class MayowebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MayowebApplication.class, args);
	}

}
