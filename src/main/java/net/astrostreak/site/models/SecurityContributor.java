package net.astrostreak.site.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityContributor implements UserDetails {

    private final Contributor contributor;

    public SecurityContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(contributor::getRole);
    }

    @Override
    public String getPassword() {
        return contributor.getPassword();
    }

    @Override
    public String getUsername() {
        return contributor.getUsername();
    }
}
