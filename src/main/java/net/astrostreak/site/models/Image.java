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

    // Builder

    public static class Builder {
        private String name;
        private String fileName;
        private String url;
        private boolean allowPublic;
        private boolean allowML;
        private Contributor contributor;

        public Builder builder() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder allowPublic(boolean allowPublic) {
            this.allowPublic = allowPublic;
            return this;
        }

        public Builder allowML(boolean allowML) {
            this.allowML = allowML;
            return this;
        }

        public Builder contributor(Contributor contributor) {
            this.contributor = contributor;
            return this;
        }

        public Image build() {
            Image image = new Image();
            image.setName(name);
            image.setFileName(fileName);
            image.setUrl(url);
            image.setAllowPublic(allowPublic);
            image.setAllowML(allowML);
            image.setContributor(contributor);
            return image;
        }
    }
}
