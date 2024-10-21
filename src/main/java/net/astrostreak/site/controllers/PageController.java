package net.astrostreak.site.controllers;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import net.astrostreak.site.services.ContributorService;
import net.astrostreak.site.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class PageController {

    private final Logger logger = Logger.getLogger(PageController.class.getName());
    private final ImageService imageService;
    private final ContributorService contributorService;

    @Autowired
    public PageController(ImageService imageService, ContributorService contributorService) {
        this.imageService = imageService;
        this.contributorService = contributorService;
    }

    @GetMapping
    public String index(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "index");
        model.addAttribute("galleryImages", imageService.indexGallery());
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "index";
    }

    @GetMapping("/contribute")
    public String contribute(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "contribute");
        model.addAttribute("formNumber", 1);
        model.addAttribute("previousError", false);
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "contribute";
    }

    @GetMapping("/gallery")
    public String gallery(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "gallery");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "gallery";
    }

    @GetMapping("/about")
    public String about(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "about");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "about";
    }

    @GetMapping("/privacy")
    public String privacy(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "privacy");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "privacy";
    }

    @GetMapping("/contact")
    public String contact(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "contact");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "contact";
    }

    @GetMapping("/license")
    public String license(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "license");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "license";
    }

    @GetMapping("/source")
    public String source(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "source");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
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

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "account");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }

            var contributor = contributorService.getContributorByUsername(authentication.getName());
            model.addAttribute("email", contributor.getEmail());
            model.addAttribute("created", contributor.getCreated());
            var imageList = imageService.imagesContributor(contributor);
            model.addAttribute("images", imageList);
        }
        return "account";
    }

    @GetMapping("/admin")
    public String admin(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "admin");
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
            model.addAttribute("contributions", imageService.getAllImagesByFileType("fits"));
        }
        return "admin";
    }

    @GetMapping("/error")
    public String error(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
            if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
            }
        }
        return "error";
    }
}
