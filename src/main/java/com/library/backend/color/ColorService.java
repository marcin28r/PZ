package com.library.backend.color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ColorService {
    @Autowired
    private ColorRepository colorRepository;

    public Optional<Color> getColorById(Long colorId){
        return colorRepository.findById(colorId);
    }

    public Color updateColor(Long id, Color updatedColor) {
        // Znajdź istniejący kolor w bazie danych
        Color existingColor = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        // Zaktualizuj wartości kolorów
        existingColor.setPrimary_(updatedColor.getPrimary_());
        existingColor.setSecondary(updatedColor.getSecondary());
        existingColor.setBackground(updatedColor.getBackground());

        // Zapisz zmieniony kolor w bazie
        return colorRepository.save(existingColor);
    }
}
