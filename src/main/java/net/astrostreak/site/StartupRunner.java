package net.astrostreak.site;

import net.astrostreak.site.models.Contributor;
import net.astrostreak.site.repositories.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Logger;

@Component
public class StartupRunner implements CommandLineRunner {

    private final Logger logger = Logger.getLogger(StartupRunner.class.getName());
    private final ContributorRepository contributorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StartupRunner(ContributorRepository contributorRepository, PasswordEncoder passwordEncoder) {
        this.contributorRepository = contributorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Checking if admin account exists");
        var admin = contributorRepository.findByUsername("fluke");
        if (admin == null) {
            admin = new Contributor();
            admin.setUsername("fluke");
            admin.setEmail("fluke@gmail.com");
            admin.setPassword(passwordEncoder.encode("fluke"));
            admin.setRole("ADMIN");
            admin.setCreated(Date.valueOf(LocalDate.now()));
            contributorRepository.save(admin);
        }
    }
}
