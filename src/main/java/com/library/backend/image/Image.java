package com.library.backend.image;

import com.library.backend.category.Category;
import com.library.backend.category.CategoryService;
import jakarta.persistence.*;
import lombok.*;

import com.library.backend.config.auth.AuthenticationResponse;
import com.library.backend.config.auth.AuthenticationService;
import com.library.backend.config.auth.RegisterRequest;
import com.library.backend.dto.BookFullDTO;
import com.library.backend.dto.MapStructMapperImpl;
import com.library.backend.rent.Rent;
import com.library.backend.rent.RentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    byte[] content;

    String name;

    String description;
}
