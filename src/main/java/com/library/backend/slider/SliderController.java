package com.library.backend.slider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slider")
public class SliderController {

    @Autowired
    private SliderService sliderService;

    @PostMapping
    public Slider createSlider(@RequestBody List<Long> imageIds) {
        return sliderService.createSlider(imageIds);
    }

    @GetMapping("/{sliderId}")
    public Slider getSlider(@PathVariable Long sliderId) {
        return sliderService.getSlider(sliderId);
    }

    @GetMapping("/")
    public List<Slider> getAllSliders() {
        return sliderService.getAllSliders();
    }

    @PutMapping("/{sliderId}")
    public Slider updateSlider(@PathVariable Long sliderId, @RequestBody List<Long> newImageIds) {
        return sliderService.updateSlider(sliderId, newImageIds);
    }

    @DeleteMapping("/{sliderId}")
    public boolean deleteSlider(@PathVariable Long sliderId) {
        return sliderService.deleteSlider(sliderId);
    }
}
