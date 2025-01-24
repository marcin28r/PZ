package com.library.backend.rent;

import com.library.backend.book.Book;
import com.library.backend.book.BookRepository;
import com.library.backend.dto.RentSlimDTO;
import com.library.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository repository;
    public final UserRepository userRepository;
    public final BookRepository bookRepository;

    public ResponseEntity<String> add(RentSlimDTO rentSlimDTO) {
        repository.save(new Rent(
                userRepository.findById(rentSlimDTO.userId()).orElseThrow(),
                bookRepository.findById(rentSlimDTO.bookId()).orElseThrow())
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public Rent load(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Rent> loadAll() {
        return repository.findAll();
    }

    public Rent take(RentSlimDTO rentSlimDTO) {
        Rent rent = repository.findByUserAndBook(userRepository.findById(rentSlimDTO.userId()).orElseThrow(),
                bookRepository.findById(rentSlimDTO.bookId()).orElseThrow()).orElseThrow();
        return rent;
    }
}
