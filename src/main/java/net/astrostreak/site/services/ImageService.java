package net.astrostreak.site.services;

import net.astrostreak.site.configurations.GalleryProperties;
import net.astrostreak.site.models.GalleryImage;
import net.astrostreak.site.models.Image;
import net.astrostreak.site.models.ImageContribution;
import net.astrostreak.site.repositories.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    public List<GalleryImage> galleryPage(Optional<String> search, Optional<Integer> page) {
        int pageNumber = page.orElse(0);
        return search.map(s -> imageRepository.findAllByAllowPublicTrueAndNameContainingIgnoreCase(PageRequest.of(pageNumber, properties.getPageSize()), s).getContent())
                .orElseGet(() -> imageRepository.findAllByAllowPublicTrue(PageRequest.of(pageNumber, properties.getPageSize())).getContent())
                .stream().map(GalleryImage::fromImage).collect(Collectors.toList());
    }

    @Transactional
    public void saveImage(ImageContribution imageContribution) {
        // Convert contribution to image entity
        var imageBuilder = new Image.Builder();
        imageContribution.getName().map(imageBuilder::name);
        imageContribution.isAllowPublic()
                .ifPresentOrElse(imageBuilder::allowPublic, () -> imageBuilder.allowPublic(false));
        imageContribution.isAllowML()
                .ifPresentOrElse(imageBuilder::allowML, () -> imageBuilder.allowML(false));

        var image = imageBuilder.build();

        // Save image entity, entity will now have database ID
        imageRepository.save(image);

        // Create file data
        var file = imageContribution.getFile().orElseThrow();
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = image.getId() + "." + fileType;
        storageService.store(file, fileName);
        image.setFileName(fileName);

        // If public create public image
        if (image.isAllowPublic()) {
            if (fileType.equals("fits")) {
                image.setUrl(storageService.getUrl("placeholder"));
            } else {
                storageService.createPublic(fileName);
                image.setUrl(storageService.getUrl(fileName));
            }
        }

        // Update image entity
        imageRepository.save(image);
    }
}
