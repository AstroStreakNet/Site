package net.astrostreak.site.services;

import net.astrostreak.site.repositories.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContributorService {

    private final ContributorRepository contributorRepository;

    @Autowired
    public ContributorService(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    public boolean isUsernameAvailable(String username) {
        return contributorRepository.findByUsername(username) == null;
    }
}
