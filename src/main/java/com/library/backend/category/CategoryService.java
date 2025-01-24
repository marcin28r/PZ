package com.library.backend.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public ResponseEntity<String> add(Category category) {
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public Category load(Long id) {
        return categoryRepository.findById(id).orElseThrow();

    }

    public List<Category> loadAll() {
        return categoryRepository.findAll();
    }
}
