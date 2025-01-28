package com.library.backend.slider;

import com.library.backend.image.Image;
import com.library.backend.image.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SliderService {
    @Autowired
    private SliderRepository sliderRepository;
    @Autowired
    private ImageRepository imageRepository;

    public Slider addImage(Long sliderId, Long imageId) {
        Optional<Slider> optionalSlider = sliderRepository.findById(sliderId);
        if (optionalSlider.isPresent()) {
            Slider slider = optionalSlider.get();
            slider.getImageIds().add(imageId);
            return sliderRepository.save(slider);
        } else {
            Slider slider = new Slider(List.of(imageId));
            return sliderRepository.save(slider);
        }
    }

    public Slider removeImage(Long sliderId, Long imageId) {
        Optional<Slider> optionalSlider = sliderRepository.findById(sliderId);
        if (optionalSlider.isPresent()) {
            Slider slider = optionalSlider.get();
            slider.getImageIds().remove(imageId);
            return sliderRepository.save(slider);
        }
        return null;
    }

    public Slider getSlider(Long sliderId) {
        return sliderRepository.findById(sliderId).orElse(null);
    }

    public List<Slider> getAllSliders() {
        return sliderRepository.findAll();
    }

    public Slider updateSlider(Long sliderId, List<Long> newImageIds) {
        Optional<Slider> optionalSlider = sliderRepository.findById(sliderId);
        if (optionalSlider.isPresent()) {
            Slider slider = optionalSlider.get();
            slider.setImageIds(newImageIds);
            return sliderRepository.save(slider);
        }
        return null;
    }

    public boolean deleteSlider(Long sliderId) {
        Optional<Slider> optionalSlider = sliderRepository.findById(sliderId);
        if (optionalSlider.isPresent()) {
            sliderRepository.delete(optionalSlider.get());
            return true;
        }
        return false;
    }

    public Slider createSlider(List<Long> imageIds) {

        Slider newSlider = new Slider();
        newSlider.setImageIds(imageIds);

        return sliderRepository.save(newSlider);
    }
}