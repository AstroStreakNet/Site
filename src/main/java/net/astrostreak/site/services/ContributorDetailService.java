package net.astrostreak.site.services;

import net.astrostreak.site.models.SecurityContributor;
import net.astrostreak.site.repositories.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ContributorDetailService implements UserDetailsService {

    private final ContributorRepository contributorRepository;

    public ContributorDetailService(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SecurityContributor(contributorRepository.findByUsername(username));
    }
}
