package com.library.backend.book;

import com.library.backend.category.Category;
import com.library.backend.category.CategoryRepository;
import com.library.backend.config.auth.AuthenticationService;
import com.library.backend.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthenticationService authenticationService;

    public ResponseEntity<String> add(String token, Book book) {
        if(Objects.equals(authenticationService.authenticatedRole(token.split(" ")[1].trim()), Role.ADMIN))
        {
            bookRepository.save(book);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    public Book load(Long id) {
         return bookRepository.findById(id).orElseThrow();
    }

    public List<Book> loadAll() {
        return bookRepository.findAll();
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
