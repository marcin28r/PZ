package com.library.backend.book;

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

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final MapStructMapperImpl mapper;
    private final BookService service;

    private final BookRepository repo;
    @PostMapping
    public ResponseEntity<String> add(@RequestHeader("Authorization") String token,@RequestBody BookFullDTO bookFullDTO){
        return service.add(token, mapper.DtoTobook(bookFullDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookFullDTO> load(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                mapper.bookToDto(service.load(id)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        service.delete(id);
        String resp = "usunięto";
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/u{id}")
    public ResponseEntity<String> update(@RequestHeader("Authorization") String token,@RequestBody BookFullDTO bookFullDTO){
        Book book = service.load(bookFullDTO.id());
        book.setAuthorName(bookFullDTO.authorName());
        book.setTitle(bookFullDTO.title());
        book.setAuthorSurname(bookFullDTO.authorSurname());
        book.setDescription(bookFullDTO.description());
        repo.save(book);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<BookFullDTO>> loadAll() {
        return new ResponseEntity<>(
                mapper.booksToDto(service.loadAll()),
                HttpStatus.OK);
    }

}
