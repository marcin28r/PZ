package com.library.backend.color;

import com.library.backend.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping(("api/color"))
public class ColorController {
    @Autowired
    ColorRepository colorRepository;

    @Autowired
    ColorService colorService;

    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        try {
            List<Color> colors = colorRepository.findAll();
            return ResponseEntity.ok(colors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Long> addColor(@RequestBody Color color) {
        try {
            // Tworzenie nowego obiektu Color i zapis do bazy
            Color dbColor = new Color();
            dbColor.setPrimary_(color.getPrimary_());
            dbColor.setSecondary(color.getSecondary());
            dbColor.setBackground(color.getBackground());

            // Zapisz kolor w bazie danych i zwróć ID nowo utworzonego rekordu
            Long colorId = colorRepository.save(dbColor).getId();

            return ResponseEntity.ok(colorId);  // Zwrócenie ID nowego koloru
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Zwrócenie błędu w przypadku problemów
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Color> updateColor(@PathVariable Long id, @RequestBody Color updatedColor) {
        try {
            // Wywołanie metody serwisowej, aby zaktualizować kolor w bazie
            Color color = colorService.updateColor(id, updatedColor);

            // Jeśli kolor został pomyślnie zaktualizowany, zwracamy status 200 OK
            return ResponseEntity.ok(color);
        } catch (Exception e) {
            // Obsługuje błąd, jeśli kolor nie zostanie znaleziony lub wystąpi problem
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteColor(@PathVariable Long id) {
        // Sprawdzamy, czy kolor istnieje w bazie
        if (!colorRepository.existsById(id)) {
            return new ResponseEntity<>("Kolor nie istnieje", HttpStatus.NOT_FOUND);
        }

        // Usuwamy kolor z bazy danych
        colorRepository.deleteById(id);

        return new ResponseEntity<>("Kolor został usunięty", HttpStatus.OK);
    }
}

