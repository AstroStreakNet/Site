package net.astrostreak.site.controllers;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import net.astrostreak.site.services.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class PageController {

    private final Logger logger = Logger.getLogger(PageController.class.getName());
    private final ImageService imageService;

    public PageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("currentPage", "index");
        model.addAttribute("galleryImages", imageService.indexGallery());
        return "index";
    }

    @GetMapping("/contribute")
    public String contribute(Model model) {
        model.addAttribute("currentPage", "contribute");
        model.addAttribute("formNumber", 1);
        model.addAttribute("previousError", false);
        return "contribute";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("currentPage", "gallery");
        return "gallery";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("currentPage", "about");
        return "about";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("currentPage", "privacy");
        return "privacy";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("currentPage", "contact");
        return "contact";
    }

    @GetMapping("/license")
    public String license(Model model) {
        model.addAttribute("currentPage", "license");
        return "license";
    }

    @GetMapping("/source")
    public String source(Model model) {
        model.addAttribute("currentPage", "source");
        return "source";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("currentPage", "login");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("currentPage", "register");
        return "register";
    }
}