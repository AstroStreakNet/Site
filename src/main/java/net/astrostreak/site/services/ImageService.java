package net.astrostreak.site.services;

import net.astrostreak.site.configurations.GalleryProperties;
import net.astrostreak.site.models.Contributor;
import net.astrostreak.site.models.GalleryImage;
import net.astrostreak.site.models.Image;
import net.astrostreak.site.models.ImageContribution;
import net.astrostreak.site.repositories.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ContributorService contributorService;
    private final StorageService storageService;
    private final GalleryProperties properties;

    @Autowired
    public ImageService(ImageRepository imageRepository, ContributorService contributorService, StorageService storageService, GalleryProperties properties) {
        this.imageRepository = imageRepository;
        this.contributorService = contributorService;
        this.storageService = storageService;
        this.properties = properties;
    }

    public List<GalleryImage> indexGallery() {
        return imageRepository.findAllByAllowPublicTrue(PageRequest.of(0, 5))
                .stream().map(GalleryImage::fromImage).collect(Collectors.toList());
    }

    public List<GalleryImage> galleryPage(Optional<String> search, Optional<Integer> page) {
        int pageNumber = page.orElse(0);
        return search.map(s -> imageRepository.findAllByAllowPublicTrueAndNameContainingIgnoreCase(PageRequest.of(pageNumber, properties.getPageSize()), s).getContent())
                .orElseGet(() -> imageRepository.findAllByAllowPublicTrue(PageRequest.of(pageNumber, properties.getPageSize())).getContent())
                .stream().map(GalleryImage::fromImage).collect(Collectors.toList());
    }

    public List<GalleryImage> imagesContributor(Contributor contributor) {
        return imageRepository.findAllByContributor(
                PageRequest.of(0, properties.getPageSize()), contributor)
                .stream().map(GalleryImage::fromImage).collect(Collectors.toList());
    }

    @Transactional
    public void saveImage(ImageContribution imageContribution, Optional<String> currentUser) {
        // Convert contribution to image entity
        var imageBuilder = new Image.Builder();
        imageContribution.getName().map(imageBuilder::name);
        imageContribution.isAllowPublic()
                .ifPresentOrElse(imageBuilder::allowPublic, () -> imageBuilder.allowPublic(false));
        imageContribution.isAllowML()
                .ifPresentOrElse(imageBuilder::allowML, () -> imageBuilder.allowML(false));

        if (currentUser.isPresent()) {
            var contributor = contributorService.getContributorByUsername(currentUser.get());
            imageBuilder.contributor(contributor);
        }

        imageBuilder.created(Date.valueOf(LocalDate.now()));

        // Create file data
        var file = imageContribution.getFile().orElseThrow();
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        imageBuilder.fileType(fileType);

        var image = imageBuilder.build();

        // Save image entity, entity will now have database ID
        imageRepository.save(image);

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

    // New method to get all images
    public List<GalleryImage> getAllImages() {
        return imageRepository.findAllByAllowPublicTrue(PageRequest.of(0, Integer.MAX_VALUE))
                .getContent().stream()
                .map(GalleryImage::fromImage)
                .collect(Collectors.toList());
    }

    public List<Image> getAllImagesByFileType(String fileType) {
        return new ArrayList<>(imageRepository.findAllByFileType(PageRequest.of(0, properties.getPageSize()), "fits")
                .getContent());
    }

    // New method to delete an image
    @Transactional
    public void deleteImage(Long id) {
        // Assuming you have a method to find an image by id
        Optional<Image> imageOpt = findImageById(id);
        imageOpt.ifPresent(image -> {
            if (image.isAllowPublic()) {
                storageService.deletePublic(image.getFileName());
            }
            storageService.deletePrivate(image.getFileName());
            // Assuming you have a method to delete an image
            deleteImageFromRepository(image);
        });
    }

    // Helper method to find an image by id
    private Optional<Image> findImageById(Long id) {
        return imageRepository.findAllByAllowPublicTrue(PageRequest.of(0, Integer.MAX_VALUE))
                .getContent().stream()
                .filter(image -> image.getId().equals(id))
                .findFirst();
    }

    // Helper method to delete an image from the repository
    private void deleteImageFromRepository(Image image) {
        // Implement the logic to delete the image from the repository
        // This might involve calling a custom method on your repository
        // or implementing the deletion logic here if the repository doesn't support it
    }
}
