package com.library.backend.slider;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Convert(converter = LongListConverter.class)
    private List<Long> imageIds;

    public Slider(List<Long> imageIds) {
        this.imageIds = imageIds;
    }
}
