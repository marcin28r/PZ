package com.library.backend.dto;

import java.util.List;

public record UserFullDTO(
        Long id,
        String username,
        String email,
        String firstname,
        String lastname,
        Integer age,
        List<RentDTO> rents
) {}
