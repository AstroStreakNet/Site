package net.astrostreak.site.services;

import net.astrostreak.site.exceptions.StorageException;
import net.astrostreak.site.exceptions.StorageFileNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface StorageService {
    void init();
    void store(MultipartFile file, String fileName) throws StorageException;
    void createPublic(String fileName);
    String getPrivate(String fileName) throws StorageFileNotFoundException;
    String getPublic(String fileName) throws StorageFileNotFoundException;
    String getUrl(String fileName) throws StorageFileNotFoundException;
    File load(String fileName) throws StorageFileNotFoundException;
    void deletePrivate(String fileName);
    void deletePublic(String fileName);
    String getFileType(String fileName);
}
