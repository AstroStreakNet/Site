package net.astrostreak.site.controllers;

import net.astrostreak.site.configurations.StorageProperties;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/public")
public class PublicContentController {

    private final StorageProperties storageProperties;

    @Autowired
    public PublicContentController(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @GetMapping("/{file}")
    public ResponseEntity<byte[]> getFile(@PathVariable String file) {
        var filePath = storageProperties.getPublicPath() + File.separator + file;
        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
