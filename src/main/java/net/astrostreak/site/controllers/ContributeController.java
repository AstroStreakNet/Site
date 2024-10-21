package net.astrostreak.site.controllers;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import net.astrostreak.site.configurations.ContributeProperties;
import net.astrostreak.site.models.ImageContribution;
import net.astrostreak.site.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@HxRequest
public class ContributeController {

    private final Logger logger = Logger.getLogger(ContributeController.class.getName());
    private final ContributeProperties properties;
    private final ImageService imageService;

    @Autowired
    public ContributeController(ContributeProperties properties, ImageService imageService) {
        this.properties = properties;
        this.imageService = imageService;
    }

    @GetMapping("/fragments/contribute")
    public HtmxResponse contribute(
            @RequestParam int formNumber,
            @RequestParam Optional<Boolean> newFile,
            Authentication authentication
    ) {
        var currentUser = authentication.getName();
        var model = Map.of("formNumber", formNumber, "currentUser", currentUser);
        if (newFile.isPresent()) {
            return HtmxResponse.builder()
                    .view(new ModelAndView("contribute-fragments :: form", model))
                    .view(new ModelAndView(
                            "contribute-fragments :: form-progress",
                            Map.of(
                                    "formNumber", formNumber,
                                    "newFile", true,
                                    "currentUser", currentUser
                            )
                    )).build();
        }
        if (formNumber >= properties.getMaxForms() - 1) {
            return HtmxResponse.builder()
                    .view(new ModelAndView("contribute-fragments :: form", model))
                    .view(new ModelAndView("contribute-fragments :: form-progress", model))
                    .build();
        }
        return HtmxResponse.builder()
                .view(new ModelAndView("contribute-fragments :: form", model))
                .view(new ModelAndView("contribute-fragments :: form-progress", model))
                .view(new ModelAndView("contribute-fragments :: add-file", model))
                .build();
    }

    @PostMapping("fragments/contribute")
    public HtmxResponse contribute(
            ImageContribution imageContribution,
            @RequestParam int formNumber,
            @RequestParam Optional<Boolean> previousError,
            @RequestParam Optional<String> contributorName
            ) {

        // Check auth status
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean correctUser = true;
        String actualUser = null;
        String username = null;
        if (authentication != null && contributorName.isPresent()) {
            actualUser = authentication.getName();
            username = contributorName.get();
            correctUser = actualUser.equals(username);
        }

        // If no file or auth status incorrect
        if (imageContribution.getFile().isEmpty() || !correctUser) {
            return HtmxResponse.builder()
                    .view(new ModelAndView(
                            "contribute-fragments :: error",
                            Map.of(
                                    "formNumber", formNumber,
                                    "previousError", previousError.isPresent()
                            )
                    )).view(new ModelAndView(
                            "contribute-fragments :: form-submit",
                            Map.of(
                                    "formNumber", formNumber,
                                    "errorOccurred", true,
                                    "currentUser", actualUser
                            )
                    )).build();
        }

        // TODO Save file
        imageService.saveImage(imageContribution, Optional.ofNullable(username));

        if (actualUser != null) {
            return HtmxResponse.builder()
                    .view(new ModelAndView(
                            "contribute-fragments :: new-file",
                            Map.of("formNumber", formNumber, "currentUser", actualUser)
                    )).view("contribute-fragments :: finished")
                    .build();
        }

        return HtmxResponse.builder()
                .view(new ModelAndView(
                        "contribute-fragments :: new-file",
                        Map.of("formNumber", formNumber)
                )).view("contribute-fragments :: finished")
                .build();
    }
}
