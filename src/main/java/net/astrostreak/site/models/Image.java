package net.astrostreak.site.models;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String fileName;
    private String url;
    private boolean allowPublic;
    private boolean allowML;
    @ManyToOne
    private Contributor contributor;

    protected Image() {}

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isAllowPublic() {
        return allowPublic;
    }

    public void setAllowPublic(boolean allowPublic) {
        this.allowPublic = allowPublic;
    }

    public boolean isAllowML() {
        return allowML;
    }

    public void setAllowML(boolean allowML) {
        this.allowML = allowML;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Image image;

        private Builder() {
            image = new Image();
        }

        public Builder name(String name) {
            image.setName(name);
            return this;
        }

        public Builder fileName(String fileName) {
            image.setFileName(fileName);
            return this;
        }

        public Builder url(String url) {
            image.setUrl(url);
            return this;
        }

        public Builder allowPublic(boolean allowPublic) {
            image.setAllowPublic(allowPublic);
            return this;
        }

        public Builder allowML(boolean allowML) {
            image.setAllowML(allowML);
            return this;
        }

        public Builder contributor(Contributor contributor) {
            image.setContributor(contributor);
            return this;
        }

        public Image build() {
            return image;
        }
    }
}