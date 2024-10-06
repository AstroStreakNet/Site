package net.astrostreak.site.configurations;

import net.astrostreak.site.repositories.ContributorRepository;
import net.astrostreak.site.services.ContributorDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserManagementConfiguration {

    private final UserManagementProperties properties;

    @Autowired
    public UserManagementConfiguration(UserManagementProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Autowired
    public UserDetailsService userDetailsService(ContributorRepository contributorRepository) {
        return new ContributorDetailService(contributorRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(properties.getPasswordStrength());
    }
}
