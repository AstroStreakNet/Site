package net.astrostreak.site.configurations;

import net.astrostreak.site.services.FileSystemStorageService;
import net.astrostreak.site.services.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Bean
    StorageService storageService(StorageProperties storageProperties) {
        var storageService = new FileSystemStorageService(storageProperties);
        storageService.init();
        return storageService;
    }
}
