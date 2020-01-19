package com.pedrogonic.swapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping(value = "/")
    public String index() {

        // TODO: Add docs
        return "TODO: Add docs";
    }

}
