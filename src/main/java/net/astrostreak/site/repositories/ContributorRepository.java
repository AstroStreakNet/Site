package net.astrostreak.site.repositories;

import net.astrostreak.site.models.Contributor;
import org.springframework.data.repository.CrudRepository;

public interface ContributorRepository extends CrudRepository<Contributor, Long> {

    Contributor findByUsername(String username);
}
