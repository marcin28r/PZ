package com.library.backend;

import com.library.backend.dto.BookFullDTO;
import com.library.backend.reflection.JsonRequestGenerator;
import org.hibernate.mapping.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

		JsonRequestGenerator generator = JsonRequestGenerator.getInstance();
		generator.generate(com.library.backend.book.BookController.class);
		generator.generate(com.library.backend.category.CategoryController.class);
		generator.generate(com.library.backend.color.ColorController.class);
		generator.generate(com.library.backend.comment.CommentController.class);
		generator.generate(com.library.backend.config.auth.AuthenticationController.class);
		generator.generate(com.library.backend.price.PriceController.class);

		generator.generate(com.library.backend.color.ColorController.class);
		generator.generate(com.library.backend.rent.RentController.class);
	}

}
