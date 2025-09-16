package com.ptit.thesis.smartrecruit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartrecruitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartrecruitApplication.class, args);
	}

}
