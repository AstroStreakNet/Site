package net.astrostreak.site.controllers;

import net.astrostreak.site.models.Contributor;
import net.astrostreak.site.services.ContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final ContributorService contributorService;

    @Autowired
    public RegisterController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("contributor", new Contributor());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Contributor contributor) {
        contributorService.registerNewContributor(contributor);
        return "redirect:/login?registered";
    }
}