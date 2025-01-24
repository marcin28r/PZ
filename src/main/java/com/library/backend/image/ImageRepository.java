package com.library.backend.image;

import com.library.backend.book.Book;
import com.library.backend.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findById(Long Long);
    List<Image> findAll();
}
