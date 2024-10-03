package net.astrostreak.site.repositories;

import net.astrostreak.site.models.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Page<Image> findAllByAllowPublicTrue(Pageable pageable);
    Page<Image> findAllByAllowPublicTrueAndNameContainingIgnoreCase(Pageable pageable, String name);
    long countByAllowPublicTrue();
    long countByAllowPublicTrueAndNameContainingIgnoreCase(String name);
}