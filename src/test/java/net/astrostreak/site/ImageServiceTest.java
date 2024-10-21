package net.astrostreak.site;

import net.astrostreak.site.configurations.GalleryProperties;
import net.astrostreak.site.models.GalleryImage;
import net.astrostreak.site.models.Image;
import net.astrostreak.site.models.ImageContribution;
import net.astrostreak.site.repositories.ImageRepository;
import net.astrostreak.site.services.ContributorService;
import net.astrostreak.site.services.ImageService;
import net.astrostreak.site.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ContributorService contributorService;

    @Mock
    private StorageService storageService;

    @Mock
    private GalleryProperties galleryProperties;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageService(imageRepository, contributorService, storageService, galleryProperties);
    }

    @Test
    void testIndexGallery() {
        List<Image> images = Arrays.asList(
            createImage(1L, "Image 1", true),
            createImage(2L, "Image 2", true)
        );
        Page<Image> page = new PageImpl<>(images);
        when(imageRepository.findAllByAllowPublicTrue(any(PageRequest.class))).thenReturn(page);

        List<GalleryImage> result = imageService.indexGallery();

        assertEquals(2, result.size());
        assertEquals("Image 1", result.get(0).getName());
        assertEquals("Image 2", result.get(1).getName());
    }

    @Test
    void testGalleryPage() {
        List<Image> images = Arrays.asList(
            createImage(1L, "Image 1", true),
            createImage(2L, "Image 2", true)
        );
        Page<Image> page = new PageImpl<>(images);
        when(imageRepository.findAllByAllowPublicTrue(any(PageRequest.class))).thenReturn(page);
        when(galleryProperties.getPageSize()).thenReturn(10);

        List<GalleryImage> result = imageService.galleryPage(Optional.empty(), Optional.of(0));

        assertEquals(2, result.size());
        assertEquals("Image 1", result.get(0).getName());
        assertEquals("Image 2", result.get(1).getName());
    }

    @Test
    void testSaveImage() throws Exception {
        ImageContribution contribution = new ImageContribution();
        contribution.setName("Test Image");
        contribution.setAllowPublic(true);
        contribution.setAllowML(false);
        MultipartFile file = mock(MultipartFile.class);
        contribution.setFile(file);
    
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        doNothing().when(imageService).saveImage(any(ImageContribution.class), Optional.empty());
        when(storageService.getUrl(anyString())).thenReturn("http://example.com/test.jpg");
    
        imageService.saveImage(contribution, Optional.empty());
    
        verify(imageRepository, times(2)).save(any(Image.class));
        verify(storageService).store(eq(file), anyString());
        verify(storageService).createPublic(anyString());
    }

    @Test
    void testGetAllImages() {
        List<Image> images = Arrays.asList(
            createImage(1L, "Image 1", true),
            createImage(2L, "Image 2", true)
        );
        Page<Image> page = new PageImpl<>(images);
        when(imageRepository.findAllByAllowPublicTrue(any(PageRequest.class))).thenReturn(page);

        List<GalleryImage> result = imageService.getAllImages();

        assertEquals(2, result.size());
        assertEquals("Image 1", result.get(0).getName());
        assertEquals("Image 2", result.get(1).getName());
    }

    @Test
    void testDeleteImage() {
        Image image = createImage(1L, "Test Image", true);
        when(imageRepository.findAllByAllowPublicTrue(any(PageRequest.class)))
            .thenReturn(new PageImpl<>(List.of(image)));

        imageService.deleteImage(1L);

        verify(storageService).deletePublic(anyString());
        verify(storageService).deletePrivate(anyString());
        // Note: verification for deleteImageFromRepository is not possible as it's a private method
    }

    private Image createImage(Long id, String name, boolean allowPublic) {
        return Image.builder()
            .name(name)
            .allowPublic(allowPublic)
            .fileName(id + ".jpg")
            .url("http://example.com/" + id + ".jpg")
            .build();
    }
}