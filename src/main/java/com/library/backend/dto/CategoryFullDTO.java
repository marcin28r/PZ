package com.library.backend.dto;

import com.library.backend.book.Book;

import java.util.List;

public record CategoryFullDTO(
        Long id,
        String name,
        List<BookFullDTO> booksToDescribe
) {}
