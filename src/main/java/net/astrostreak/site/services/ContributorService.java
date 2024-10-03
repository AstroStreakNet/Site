package net.astrostreak.site.services;

import net.astrostreak.site.models.Contributor;
import net.astrostreak.site.repositories.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ContributorService {

    private final ContributorRepository contributorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ContributorService(ContributorRepository contributorRepository, PasswordEncoder passwordEncoder) {
        this.contributorRepository = contributorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUsernameAvailable(String username) {
        return contributorRepository.findByUsername(username) == null;
    }

    public void registerNewContributor(Contributor contributor) {
        contributor.setPassword(passwordEncoder.encode(contributor.getPassword()));
        contributor.setRole("ROLE_USER");
        contributorRepository.save(contributor);
    }
}