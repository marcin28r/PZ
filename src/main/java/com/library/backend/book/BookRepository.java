package com.library.backend.book;

import com.library.backend.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long Long);
    List<Book> findAll();
    List<Book> findByCategory(Category category);
}
