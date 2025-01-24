package com.library.backend.rent;

import com.library.backend.book.Book;
import com.library.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Long> {

    Optional<Rent> findByUserAndBook(User user, Book book);
}
