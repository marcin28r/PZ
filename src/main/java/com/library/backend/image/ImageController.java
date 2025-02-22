package com.library.backend.image;

import com.library.backend.dto.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(("/api/image"))
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping
    public Long uploadImage(
            @RequestParam MultipartFile multipartImage,
            @RequestParam String name,
            @RequestParam String description
    ) throws Exception {
        Image dbImage = new Image();
        dbImage.setName(name);
        dbImage.setDescription(description);
        dbImage.setContent(multipartImage.getBytes());

        return imageRepository.save(dbImage).getId();
    }


    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllImages() {
        List<Image> images = imageService.getAllImages();

        List<Map<String, String>> response = new ArrayList<>();

        for (Image image : images) {
            byte[] imageContent = image.getContent();
            String imageBase64 = Base64.getEncoder().encodeToString(imageContent);

            Map<String, String> imageData = new HashMap<>();
            imageData.put("id", image.getId().toString());
            imageData.put("imageBase64", imageBase64);
            imageData.put("name", image.getName());
            imageData.put("description", image.getDescription());

            response.add(imageData);
        }

        return ResponseEntity.ok(response);
    }





    @Autowired
    private ImageService imageService;



    @GetMapping("/{imageId}")
    public ResponseEntity<Map<String, String>> downloadImage(@PathVariable Long imageId) {
        Image image = imageService.getImageById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));

        byte[] imageContent = image.getContent();

        String imageBase64 = Base64.getEncoder().encodeToString(imageContent);

        Map<String, String> response = new HashMap<>();
        response.put("imageBase64", imageBase64);
        response.put("name", image.getName());
        response.put("description", image.getDescription());

        return ResponseEntity.ok(response);
    }

}