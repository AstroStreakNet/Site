package net.astrostreak.site.controllers;

import net.astrostreak.site.models.GalleryImage;
import net.astrostreak.site.models.api.UploadRequest;
import net.astrostreak.site.models.api.UploadResponse;
import net.astrostreak.site.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ImageService imageService;

    @Autowired
    public ApiController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/image")
    public UploadResponse upload(
            @RequestParam("file") MultipartFile file, @RequestBody UploadRequest request
    ) {
        var response = new UploadResponse();
        response.setStatus("success");
        return response;
    }

    @GetMapping("/image")
    public List<GalleryImage> getImages() {
        return List.of();
    }
}
