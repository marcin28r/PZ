package com.library.backend.dto;

public class ImageDTO {
    private Long id;
    private String name;
    private String description;
    private String base64Content;

    public ImageDTO(Long id, String name, String description, String base64Content) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.base64Content = base64Content;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }
}
