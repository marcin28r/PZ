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
            Color dbColor = new Color();
            dbColor.setPrimary_(color.getPrimary_());
            dbColor.setSecondary(color.getSecondary());
            dbColor.setBackground(color.getBackground());

            Long colorId = colorRepository.save(dbColor).getId();

            return ResponseEntity.ok(colorId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Color> updateColor(@PathVariable Long id, @RequestBody Color updatedColor) {
        try {
            Color color = colorService.updateColor(id, updatedColor);

            return ResponseEntity.ok(color);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteColor(@PathVariable Long id) {
        if (!colorRepository.existsById(id)) {
            return new ResponseEntity<>("Kolor nie istnieje", HttpStatus.NOT_FOUND);
        }

        colorRepository.deleteById(id);

        return new ResponseEntity<>("Kolor został usunięty", HttpStatus.OK);
    }
}

