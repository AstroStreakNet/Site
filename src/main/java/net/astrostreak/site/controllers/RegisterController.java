package net.astrostreak.site.controllers;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import net.astrostreak.site.services.ContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.logging.Logger;

@Controller
@HxRequest
public class RegisterController {

    private final Logger logger = Logger.getLogger(RegisterController.class.getName());
    private final ContributorService contributorService;

    @Autowired
    public RegisterController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @PostMapping("/fragments/username")
    public HtmxResponse username(@RequestParam String username) {
        var model = Map.of("usernameValue", username);
        if (contributorService.isUsernameAvailable(username)) {
            return HtmxResponse.builder()
                    .view(new ModelAndView(
                            "register-fragments :: username-available",
                            model
                    )).build();
        }
        return HtmxResponse.builder().view(new ModelAndView(
                "register-fragments :: username-taken",
                        model
                )).build();
    }
}
