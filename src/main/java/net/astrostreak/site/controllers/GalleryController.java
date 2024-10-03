package net.astrostreak.site.controllers;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import net.astrostreak.site.configurations.GalleryProperties;
import net.astrostreak.site.models.GalleryImage;
import net.astrostreak.site.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@HxRequest
public class GalleryController {

    private final ImageService imageService;
    private final GalleryProperties galleryProperties;

    @Autowired
    public GalleryController(ImageService imageService, GalleryProperties galleryProperties) {
        this.imageService = imageService;
        this.galleryProperties = galleryProperties;
    }

    @GetMapping("/fragments/gallery")
    public HtmxResponse gallery(@RequestParam Optional<String> search, @RequestParam Optional<Integer> page) {
        int pageNumber = page.orElse(0);
        List<GalleryImage> images = imageService.galleryPage(search, pageNumber, galleryProperties.getPageSize()).getContent();
        return HtmxResponse.builder()
                .view(new ModelAndView(
                        "gallery-fragments :: image",
                        Map.of("images", images, "currentSearch", search))
                ).build();
    }
}