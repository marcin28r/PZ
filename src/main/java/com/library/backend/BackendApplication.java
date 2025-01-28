package com.library.backend;

import com.library.backend.reflection.JsonRequestGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		JsonRequestGenerator generator = new JsonRequestGenerator();
		generator.generate(com.library.backend.book.BookController.class);
		generator.generate(com.library.backend.config.auth.AuthenticationController.class);
	}

}
