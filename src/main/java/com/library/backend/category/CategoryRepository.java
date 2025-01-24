package com.library.backend.category;

import com.library.backend.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long aLong);
    Set<Category> findByBooksToDescribe(Book book);

}
