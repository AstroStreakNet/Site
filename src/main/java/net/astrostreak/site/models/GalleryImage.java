package net.astrostreak.site.models;

public class GalleryImage {
    private Long id;
    private String name;
    private String url;
    private Contributor contributor;
    private boolean allowPublic;
    private boolean allowML;

    public GalleryImage() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public boolean getAllowPublic() {
        return allowPublic;
    }

    public void setAllowPublic(boolean allowPublic) {
        this.allowPublic = allowPublic;
    }

    public boolean getAllowML() {
        return allowML;
    }

    public void setAllowML(boolean allowML) {
        this.allowML = allowML;
    }

    public static GalleryImage fromImage(Image image) {
        var galleryImage = new GalleryImage();
        galleryImage.setId(image.getId());
        galleryImage.setName(image.getName());
        galleryImage.setUrl(image.getUrl());
        galleryImage.setContributor(image.getContributor());
        galleryImage.setAllowPublic(image.isAllowPublic());
        galleryImage.setAllowML(image.isAllowML());
        return galleryImage;
    }
}