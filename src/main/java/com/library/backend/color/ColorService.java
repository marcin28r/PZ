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
        Color existingColor = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        existingColor.setPrimary_(updatedColor.getPrimary_());
        existingColor.setSecondary(updatedColor.getSecondary());
        existingColor.setBackground(updatedColor.getBackground());

        return colorRepository.save(existingColor);
    }
}
