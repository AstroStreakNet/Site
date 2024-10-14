package net.astrostreak.site.models;

import jakarta.persistence.*;
import java.util.Objects;

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

    // Protected constructor for JPA
    protected Image() {}

    // Private constructor for builder
    private Image(Builder builder) {
        this.name = builder.name;
        this.fileName = builder.fileName;
        this.url = builder.url;
        this.allowPublic = builder.allowPublic;
        this.allowML = builder.allowML;
        this.contributor = builder.contributor;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public boolean isAllowPublic() {
        return allowPublic;
    }

    public boolean isAllowML() {
        return allowML;
    }

    public Contributor getContributor() {
        return contributor;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAllowPublic(boolean allowPublic) {
        this.allowPublic = allowPublic;
    }

    public void setAllowML(boolean allowML) {
        this.allowML = allowML;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String fileName;
        private String url;
        private boolean allowPublic;
        private boolean allowML;
        private Contributor contributor;

        private Builder() {}

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
            return new Image(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return allowPublic == image.allowPublic &&
               allowML == image.allowML &&
               Objects.equals(id, image.id) &&
               Objects.equals(name, image.name) &&
               Objects.equals(fileName, image.fileName) &&
               Objects.equals(url, image.url) &&
               Objects.equals(contributor, image.contributor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fileName, url, allowPublic, allowML, contributor);
    }

    @Override
    public String toString() {
        return "Image{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", fileName='" + fileName + '\'' +
               ", url='" + url + '\'' +
               ", allowPublic=" + allowPublic +
               ", allowML=" + allowML +
               ", contributor=" + contributor +
               '}';
    }
}