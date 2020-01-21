package com.pedrogonic.swapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class MainController {

    @GetMapping(value = "/", produces = "text/html; charset=UTF-8")
    public RedirectView index() {

        String url = "swagger-ui.html";

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);

        return redirectView;
    }

}
