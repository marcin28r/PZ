package com.library.backend.price;

import com.library.backend.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
