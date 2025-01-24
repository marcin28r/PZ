package com.library.backend.dto;

import java.util.List;


public record BookFullDTO(
        Long id,
        String image,
        String authorName,
        String authorSurname,
        String title,
        String description,
        List<CategoryDTO> categories
) {}
