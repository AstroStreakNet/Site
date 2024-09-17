package net.astrostreak.site.models;

public class GalleryImage {
    private Long id;
    private String name;
    private String url;
    private Contributor contributor;

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

    // Converter

    public static GalleryImage fromImage(Image image) {
        var galleryImage = new GalleryImage();
        galleryImage.setId(image.getId());
        galleryImage.setName(image.getName());
        galleryImage.setUrl(image.getUrl());
        galleryImage.setContributor(image.getContributor());
        return galleryImage;
    }
}