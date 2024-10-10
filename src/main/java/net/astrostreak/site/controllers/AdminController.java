package net.astrostreak.site.controllers;

import net.astrostreak.site.models.Contributor;
import net.astrostreak.site.repositories.ContributorRepository;
import net.astrostreak.site.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ImageService imageService;
    private final ContributorRepository contributorRepository;

    @Autowired
    public AdminController(ImageService imageService, ContributorRepository contributorRepository) {
        this.imageService = imageService;
        this.contributorRepository = contributorRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("images", imageService.getAllImages());
        model.addAttribute("users", contributorRepository.findAll());
        return "admin";
    }

    @PostMapping("/deleteImage/{id}")
    public String deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return "redirect:/admin";
    }

    @PostMapping("/updateUserRole/{id}")
    public String updateUserRole(@PathVariable Long id, @RequestParam String role) {
        // This method might need to be adjusted based on your ContributorRepository implementation
        Contributor contributor = contributorRepository.findByUsername(id.toString());
        if (contributor != null) {
            contributor.setRole(role);
            contributorRepository.save(contributor);
        }
        return "redirect:/admin";
    }
}