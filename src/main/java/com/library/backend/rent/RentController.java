package com.library.backend.rent;

import com.library.backend.book.BookService;
import com.library.backend.config.auth.AuthenticationService;
import com.library.backend.dto.BookFullDTO;
import com.library.backend.dto.MapStructMapperImpl;
import com.library.backend.dto.RentDTO;
import com.library.backend.dto.RentSlimDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rent")
@RequiredArgsConstructor
public class RentController {
    private final MapStructMapperImpl mapper;
    private final RentService service;
    private final AuthenticationService auth;
    private final RentRepository repo;

    @PostMapping
    public ResponseEntity<String> add(@RequestBody RentSlimDTO rentSlimDTO){
        return service.add(rentSlimDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentDTO> load(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                mapper.rentToDto(
                        service.load(id)
                ),
                HttpStatus.OK
        );
    }

    @PutMapping()
    public ResponseEntity<String> take(@RequestHeader("Authorization") String token,@RequestBody RentSlimDTO rentSlimDTO) {
        if(auth.authenticatedAdmin(token)){
            Rent rent = service.take(rentSlimDTO);
            rent.setTaken(LocalDateTime.now());
            repo.save(rent);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
