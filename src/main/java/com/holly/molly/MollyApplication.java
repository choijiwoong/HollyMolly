package com.holly.molly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MollyApplication {
	public static void main(String[] args) {
		SpringApplication.run(MollyApplication.class, args);
	}
}