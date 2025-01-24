package com.library.backend.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository; // Repository do komunikacji z bazą danych

    // Pobieranie wszystkich zdjęć z bazy danych
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Optional<Image> getImageById(Long imageId) {
        return imageRepository.findById(imageId);
    }
}
