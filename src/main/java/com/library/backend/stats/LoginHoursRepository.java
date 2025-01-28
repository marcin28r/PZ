package com.library.backend.stats;

import com.library.backend.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LoginHoursRepository extends JpaRepository<LoginHours, Long> {

    Optional<LoginHours> findById(Long Long);
    List<LoginHours> findAll();
}
