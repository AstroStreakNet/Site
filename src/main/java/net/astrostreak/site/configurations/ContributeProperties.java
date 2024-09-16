package net.astrostreak.site.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "contribute")
public class ContributeProperties {
    private int maxForms;

    public int getMaxForms() {
        return maxForms;
    }

    public void setMaxForms(int maxForms) {
        this.maxForms = maxForms;
    }
}
