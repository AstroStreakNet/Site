package net.astrostreak.site.services;

import net.astrostreak.site.exceptions.RegisterException;
import net.astrostreak.site.models.Contributor;
import net.astrostreak.site.models.ContributorDetails;
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

    public void createContributor(ContributorDetails contributorDetails) throws RegisterException {
        Contributor contributor = new Contributor();

        // validate
        if (!contributorDetails.isValid()) {
            throw new RegisterException("invalid");
        }

        // Check if username available
        if (!isUsernameAvailable(contributorDetails.getUsername())) {
            throw new RegisterException("username");
        }
        contributor.setUsername(contributorDetails.getUsername());
        contributor.setEmail(contributorDetails.getEmail());

        // Encode password
        var password = passwordEncoder.encode(contributorDetails.getPassword());
        contributor.setPassword(password);

        // Set role
        contributor.setRole("USER");

        // Save
        contributorRepository.save(contributor);
    }
}
