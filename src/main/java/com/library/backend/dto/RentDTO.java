package com.library.backend.dto;

import com.library.backend.book.Book;
import com.library.backend.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public record RentDTO(
        Long id,
        UserSlimDTO user,
        BookFullDTO book,
        LocalDateTime borrow,
        LocalDateTime taken
) {
}
