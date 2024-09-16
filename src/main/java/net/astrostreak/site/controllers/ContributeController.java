package net.astrostreak.site.controllers;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import net.astrostreak.site.configurations.ContributeProperties;
import net.astrostreak.site.models.ImageContribution;
import net.astrostreak.site.services.ImageService;
import net.astrostreak.site.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller
@HxRequest
public class ContributeController {

    private final ContributeProperties properties;
    private final ImageService imageService;

    @Autowired
    public ContributeController(ContributeProperties properties, ImageService imageService) {
        this.properties = properties;
        this.imageService = imageService;
    }

    @GetMapping("/fragments/contribute")
    public HtmxResponse contribute(@RequestParam int formNumber, @RequestParam Optional<Boolean> newFile) {
        var model = Map.of("formNumber", formNumber);
        if (newFile.isPresent()) {
            return HtmxResponse.builder()
                    .view(new ModelAndView("contribute-fragments :: form", model))
                    .view(new ModelAndView(
                            "contribute-fragments :: form-progress",
                            Map.of("formNumber", formNumber, "newFile", true)
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
            @RequestParam Optional<Boolean> previousError
            ) {
        if (imageContribution.getFile().isEmpty()) {
            return HtmxResponse.builder()
                    .view(new ModelAndView(
                            "contribute-fragments :: error",
                            Map.of("formNumber", formNumber, "previousError", previousError.isPresent())
                    )).view(new ModelAndView(
                            "contribute-fragments :: form-submit",
                            Map.of("formNumber", formNumber, "errorOccurred", true)
                    )).build();
        }

        // TODO Save file
        imageService.saveImage(imageContribution);

        return HtmxResponse.builder()
                .view(new ModelAndView(
                        "contribute-fragments :: new-file",
                        Map.of("formNumber", formNumber)
                )).view("contribute-fragments :: finished")
                .build();
    }
}
