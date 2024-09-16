package net.astrostreak.site.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="storage")
public class StorageProperties {
    private String privatePath;
    private String publicPath;
    private String urlPath;
    private String previewFileType;
    private int previewFileWidth;

    public String getPrivatePath() {
        return privatePath;
    }

    public void setPrivatePath(String privatePath) {
        this.privatePath = privatePath;
    }

    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getPreviewFileType() {
        return previewFileType;
    }

    public void setPreviewFileType(String previewFileType) {
        this.previewFileType = previewFileType;
    }

    public int getPreviewFileWidth() {
        return previewFileWidth;
    }

    public void setPreviewFileWidth(int previewFileWidth) {
        this.previewFileWidth = previewFileWidth;
    }
}
