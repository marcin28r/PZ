package com.library.backend.category;

import com.library.backend.dto.CategoryDTO;
import com.library.backend.dto.CategoryFullDTO;
import com.library.backend.dto.MapStructMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final MapStructMapperImpl mapper;

    @PostMapping
    public ResponseEntity<String> add(@RequestBody CategoryDTO categoryDTO ){
        return service.add(mapper.DtoToCategory(categoryDTO));
    }

    @GetMapping("/")
    public ResponseEntity<CategoryDTO> load(@RequestParam("id") Long id) {
        return new ResponseEntity<>(
                mapper.categoryToDto(
                        service.load(id)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> loadAll() {
        return new ResponseEntity<>(
                mapper.categoriesToDto(service.loadAll()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<CategoryFullDTO> loadFull(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                mapper.categoryToFullDto(
                        service.load(id)
                ),
                HttpStatus.OK
        );
    }
}
