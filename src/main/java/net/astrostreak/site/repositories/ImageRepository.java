package net.astrostreak.site.repositories;

import net.astrostreak.site.models.Contributor;
import net.astrostreak.site.models.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

    void save(Image image);
    Page<Image> findAllByAllowPublicTrue(Pageable pageable);
    Page<Image> findAllByAllowPublicTrueAndNameContainingIgnoreCase(Pageable pageable, String name);
    Page<Image> findAllByContributor(Pageable pageable, Contributor contributor);
}
