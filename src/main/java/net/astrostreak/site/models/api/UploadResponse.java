package net.astrostreak.site.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadResponse {
    private String status;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
