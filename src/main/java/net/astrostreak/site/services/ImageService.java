package net.astrostreak.site.services;

import net.astrostreak.site.configurations.GalleryProperties;
import net.astrostreak.site.models.GalleryImage;
import net.astrostreak.site.models.Image;
import net.astrostreak.site.models.ImageContribution;
import net.astrostreak.site.repositories.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final StorageService storageService;
    private final GalleryProperties properties;

    @Autowired
    public ImageService(ImageRepository imageRepository, StorageService storageService, GalleryProperties properties) {
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.properties = properties;
    }

    public List<GalleryImage> indexGallery() {
        return imageRepository.findAllByAllowPublicTrue(PageRequest.of(0, 5))
                .stream().map(GalleryImage::fromImage).collect(Collectors.toList());
    }

    public Page<GalleryImage> galleryPage(Optional<String> search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Image> imagePage = search.map(s -> imageRepository.findAllByAllowPublicTrueAndNameContainingIgnoreCase(pageRequest, s))
                .orElseGet(() -> imageRepository.findAllByAllowPublicTrue(pageRequest));
        return imagePage.map(GalleryImage::fromImage);
    }

    @Transactional
    public Image saveImage(ImageContribution imageContribution) {
        Image image = Image.builder()
                .name(imageContribution.getName().orElse("Untitled"))
                .allowPublic(imageContribution.isAllowPublic().orElse(false))
                .allowML(imageContribution.isAllowML().orElse(false))
                .build();

        // Save image entity to get an ID
        image = imageRepository.save(image);

        // Handle file storage
        var file = imageContribution.getFile().orElseThrow(() -> new IllegalArgumentException("File is required"));
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = image.getId() + "." + fileType;
        storageService.store(file, fileName);
        image.setFileName(fileName);

        // Create public image if allowed
        if (image.isAllowPublic()) {
            if ("fits".equalsIgnoreCase(fileType)) {
                image.setUrl(storageService.getUrl("placeholder"));
            } else {
                storageService.createPublic(fileName);
                image.setUrl(storageService.getUrl(fileName));
            }
        }

        // Update image entity with file information
        return imageRepository.save(image);
    }

    public Optional<GalleryImage> getImageById(Long id) {
        return imageRepository.findById(id).map(GalleryImage::fromImage);
    }

    @Transactional
    public boolean deleteImage(Long id) {
        return imageRepository.findById(id).map(image -> {
            storageService.deletePrivate(image.getFileName());
            if (image.isAllowPublic()) {
                storageService.deletePublic(image.getFileName());
            }
            imageRepository.delete(image);
            return true;
        }).orElse(false);
    }

    @Transactional
    public Optional<GalleryImage> updateImage(Long id, GalleryImage updatedImage) {
        return imageRepository.findById(id)
            .map(image -> {
                image.setName(updatedImage.getName());
                image.setAllowPublic(updatedImage.getAllowPublic());
                image.setAllowML(updatedImage.getAllowML());
                
                if (image.isAllowPublic() != updatedImage.getAllowPublic()) {
                    if (updatedImage.getAllowPublic()) {
                        storageService.createPublic(image.getFileName());
                        image.setUrl(storageService.getUrl(image.getFileName()));
                    } else {
                        storageService.deletePublic(image.getFileName());
                        image.setUrl(null);
                    }
                }

                return GalleryImage.fromImage(imageRepository.save(image));
            });
    }

    public long getImageCount(Optional<String> search) {
        return search.map(imageRepository::countByAllowPublicTrueAndNameContainingIgnoreCase)
                .orElseGet(imageRepository::countByAllowPublicTrue);
    }
}