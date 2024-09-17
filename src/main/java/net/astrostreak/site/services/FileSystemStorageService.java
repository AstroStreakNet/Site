package net.astrostreak.site.services;

import net.astrostreak.site.configurations.StorageProperties;
import net.astrostreak.site.exceptions.StorageException;
import net.astrostreak.site.exceptions.StorageFileNotFoundException;
import nom.tam.fits.Fits;
import nom.tam.fits.ImageHDU;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileSystemStorageService implements StorageService {

    private final StorageProperties properties;

    public FileSystemStorageService(StorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public void init() {
        Path publicPath = Paths.get(properties.getPublicPath());
        if (!Files.exists(publicPath)) {
            throw new StorageException("Provided public path does not exist: " + publicPath);
        }
        Path privatePath = Paths.get(properties.getPrivatePath());
        if (!Files.exists(privatePath)) {
            throw new StorageException("Provided private path does not exist: " + privatePath);
        }
    }

    @Override
    public void store(MultipartFile file, String fileName) throws StorageException {
        File newFile = new File(properties.getPrivatePath() + File.separator + fileName);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            throw new StorageException("Could not store file: " + fileName, e);
        }
    }

    @Override
    public void createPublic(String fileName) {
        File file = load(fileName);
        BufferedImage preview = switch (getFileType(fileName).toLowerCase()) {
            case "fits" -> previewFromFITS(file);
            case "jpeg", "jpg", "png" -> previewFromNormal(file);
            default -> throw new StorageException("Unexpected file type: " + getFileType(fileName).toLowerCase());
        };

        var outputFilePath = properties.getPublicPath() +
                File.separator +
                FilenameUtils.removeExtension(fileName) +
                "." +
                properties.getPreviewFileType();
        File outputFile = new File(outputFilePath);

        try {
            ImageIO.write(preview, "jpg", outputFile);
        } catch (IOException e) {
            throw new StorageException("Could not create public preview: " + fileName, e);
        }
    }

    @Override
    public String getPrivate(String fileName) throws StorageFileNotFoundException {
        try (Stream<Path> stream = Files.list(Paths.get(properties.getPrivatePath()))) {
            return stream.filter(f -> f.getFileName().toString().equals(fileName))
                    .findFirst()
                    .map(f -> f.toAbsolutePath().toString())
                    .orElseThrow(() -> new StorageFileNotFoundException("Can't find " + fileName));
        } catch (IOException e) {
            throw new StorageException("Error opening " + properties.getPrivatePath(), e);
        }
    }

    @Override
    public String getPublic(String fileName) throws StorageFileNotFoundException {
        var newFileName = FilenameUtils.removeExtension(fileName) + properties.getPreviewFileType();
        try (Stream<Path> stream = Files.list(Paths.get(properties.getPublicPath()))) {
            return stream.filter(f -> f.getFileName().toString().equals(newFileName))
                    .findFirst()
                    .map(f -> f.toAbsolutePath().toString())
                    .orElseThrow(() -> new StorageFileNotFoundException("Can't find " + newFileName));
        } catch (IOException e) {
            throw new StorageException("Error opening " + properties.getPublicPath(), e);
        }
    }

    @Override
    public String getUrl(String fileName) throws StorageFileNotFoundException {
        return properties.getUrlPath() +
                File.separator +
                FilenameUtils.removeExtension(fileName) +
                "." + properties.getPreviewFileType();
    }

    @Override
    public File load(String fileName) throws StorageFileNotFoundException {
        File file = new File(getPrivate(fileName));
        if (!file.exists()) {
            throw new StorageFileNotFoundException("Can't find " + fileName);
        }
        return file;
    }

    @Override
    public void deletePrivate(String fileName) {
        File file = new File(getPrivate(fileName));
        boolean operation = file.delete();
        if (!operation) {
            throw new StorageFileNotFoundException("Can't delete " + fileName);
        }
    }

    @Override
    public void deletePublic(String fileName) {
        File file = new File(getPublic(fileName));
        boolean operation = file.delete();
        if (!operation) {
            throw new StorageFileNotFoundException("Can't delete " + fileName);
        }
    }

    @Override
    public String getFileType(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    // Preview generation

    public BufferedImage previewFromFITS(File fileObject) {
        try (Fits file = new Fits(fileObject)) {
            // Open file and get data
            var arrayHDU = file.read();
            ImageHDU hdu;
            if (arrayHDU.length > 1) {
                hdu = (ImageHDU) file.getHDU(1);
            } else {
                hdu = (ImageHDU) file.getHDU(0);
            }
            int[][] imageData = (int[][]) hdu.getData().convertTo(int.class).getKernel();
            int width = imageData[0].length;
            int height = imageData.length;
            // Convert data to image
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            WritableRaster raster = (WritableRaster) image.getData();
            raster.setPixels(0, 0, width-1, height-1, Arrays.stream(imageData).flatMapToInt(Arrays::stream).toArray());
            // Resize image
            return Scalr.resize(image, properties.getPreviewFileWidth());
        } catch (IOException e) {
            throw new StorageException("Error generating preview " + fileObject.getName(), e);
        }
    }

    public BufferedImage previewFromNormal(File fileObject) {
        try {
            BufferedImage image = ImageIO.read(fileObject);
            return Scalr.resize(image, properties.getPreviewFileWidth());
        } catch (IOException e) {
            throw new StorageException("Error generating preview " + fileObject.getName(), e);
        }
    }
}
