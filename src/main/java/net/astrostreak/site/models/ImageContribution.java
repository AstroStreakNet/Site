package net.astrostreak.site.models;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

public class ImageContribution {
    private MultipartFile file;
    private String name;
    private LocalDateTime julianDate;
    private String observatoryCode;
    private Float rightAscension;
    private Float declination;
    private Float exposureDuration;
    private String streakType;
    private Boolean allowPublic;
    private Boolean allowML;

    public ImageContribution() {}

    public Optional<MultipartFile> getFile() {
        return Optional.ofNullable(file);
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<LocalDateTime> getJulianDate() {
        return Optional.ofNullable(julianDate);
    }

    public void setJulianDate(LocalDateTime julianDate) {
        this.julianDate = julianDate;
    }

    public Optional<String> getObservatoryCode() {
        return Optional.ofNullable(observatoryCode);
    }

    public void setObservatoryCode(String observatoryCode) {
        this.observatoryCode = observatoryCode;
    }

    public Optional<Float> getRightAscension() {
        return Optional.ofNullable(rightAscension);
    }

    public void setRightAscension(Float rightAscension) {
        this.rightAscension = rightAscension;
    }

    public Optional<Float> getDeclination() {
        return Optional.ofNullable(declination);
    }

    public void setDeclination(Float declination) {
        this.declination = declination;
    }

    public Optional<Float> getExposureDuration() {
        return Optional.ofNullable(exposureDuration);
    }

    public void setExposureDuration(Float exposureDuration) {
        this.exposureDuration = exposureDuration;
    }

    public Optional<String> getStreakType() {
        return Optional.ofNullable(streakType);
    }

    public void setStreakType(String streakType) {
        this.streakType = streakType;
    }

    public Optional<Boolean> isAllowPublic() {
        return Optional.ofNullable(allowPublic);
    }

    public void setAllowPublic(Boolean allowPublic) {
        this.allowPublic = allowPublic;
    }

    public Optional<Boolean> isAllowML() {
        return Optional.ofNullable(allowML);
    }

    public void setAllowML(Boolean allowML) {
        this.allowML = allowML;
    }
}
