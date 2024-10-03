package net.astrostreak.site.controllers;

import net.astrostreak.site.models.GalleryImage;
import net.astrostreak.site.models.ImageContribution;
import net.astrostreak.site.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ApiController {

    private final ImageService imageService;

    @Autowired
    public ApiController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryImage> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam(value = "observatoryCode", required = false) String observatoryCode,
            @RequestParam(value = "julianDate", required = false) String julianDate,
            @RequestParam(value = "rightAscension", required = false) Float rightAscension,
            @RequestParam(value = "declination", required = false) Float declination,
            @RequestParam(value = "exposureDuration", required = false) Float exposureDuration,
            @RequestParam(value = "streakType", required = false) String streakType,
            @RequestParam(value = "allowPublic", defaultValue = "false") Boolean allowPublic,
            @RequestParam(value = "allowML", defaultValue = "false") Boolean allowML) {

        ImageContribution contribution = new ImageContribution();
        contribution.setFile(file);
        contribution.setName(name);
        contribution.setObservatoryCode(observatoryCode);
        contribution.setJulianDate(julianDate != null ? java.time.LocalDateTime.parse(julianDate) : null);
        contribution.setRightAscension(rightAscension);
        contribution.setDeclination(declination);
        contribution.setExposureDuration(exposureDuration);
        contribution.setStreakType(streakType);
        contribution.setAllowPublic(allowPublic);
        contribution.setAllowML(allowML);

        try {
            GalleryImage savedImage = GalleryImage.fromImage(imageService.saveImage(contribution));
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<GalleryImage>> getImages(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        Page<GalleryImage> images = imageService.galleryPage(Optional.ofNullable(search), page, size);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryImage> getImage(@PathVariable Long id) {
        return imageService.getImageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        boolean deleted = imageService.deleteImage(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryImage> updateImage(
            @PathVariable Long id,
            @RequestBody GalleryImage updatedImage) {
        return imageService.updateImage(id, updatedImage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getImageCount(
            @RequestParam(value = "search", required = false) String search) {
        long count = imageService.getImageCount(Optional.ofNullable(search));
        return ResponseEntity.ok(count);
    }
}