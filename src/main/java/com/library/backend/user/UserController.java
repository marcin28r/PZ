package com.library.backend.user;

import com.library.backend.color.Color;
import com.library.backend.dto.longReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/color")
class UserController {

    @Autowired
    private UserService userService;

    @PutMapping()
    public ResponseEntity<String> updateColorToUser(@RequestHeader("Authorization") String token, @RequestBody longReqDTO req){
        return userService.authService.putColorId(token, req.id());
    }

    @GetMapping("/userColor")
    public ResponseEntity<Color> getUserColor(@RequestHeader("Authorization") String token) {
        try {
            // Pobierz przypisany kolor do użytkownika za pomocą serwisu
            Color userColor = userService.authService.getUserColor(token)
                    .orElseThrow(() -> new RuntimeException("Kolor nie został znaleziony"));

            // Zwróć kolor użytkownika
            return ResponseEntity.ok(userColor);

        } catch (Exception e) {
            // Obsługa błędów
            return ResponseEntity.status(500).body(null);
        }
    }


}
