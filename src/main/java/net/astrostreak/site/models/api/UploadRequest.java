package net.astrostreak.site.models.api;

import java.time.LocalDateTime;

public class UploadRequest {
    private String name;
    private String observatoryCode;
    private LocalDateTime julianDate;
    private Float rightAscension;
    private Float declination;
    private Float exposureDuration;
    private String streakType;
    private Boolean allowPublic;
    private Boolean allowML;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObservatoryCode() {
        return observatoryCode;
    }

    public void setObservatoryCode(String observatoryCode) {
        this.observatoryCode = observatoryCode;
    }

    public LocalDateTime getJulianDate() {
        return julianDate;
    }

    public void setJulianDate(LocalDateTime julianDate) {
        this.julianDate = julianDate;
    }

    public float getRightAscension() {
        return rightAscension;
    }

    public void setRightAscension(float rightAscension) {
        this.rightAscension = rightAscension;
    }

    public float getDeclination() {
        return declination;
    }

    public void setDeclination(float declination) {
        this.declination = declination;
    }

    public float getExposureDuration() {
        return exposureDuration;
    }

    public void setExposureDuration(float exposureDuration) {
        this.exposureDuration = exposureDuration;
    }

    public String getStreakType() {
        return streakType;
    }

    public void setStreakType(String streakType) {
        this.streakType = streakType;
    }

    public Boolean getAllowPublic() {
        return allowPublic;
    }

    public void setAllowPublic(Boolean allowPublic) {
        this.allowPublic = allowPublic;
    }

    public Boolean getAllowML() {
        return allowML;
    }

    public void setAllowML(Boolean allowML) {
        this.allowML = allowML;
    }
}
