package com.pedrogonic.swapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.LogManager;
import java.util.logging.Logger;

@SpringBootApplication
@Slf4j
public class SwapiApplication {

    public static void main(String[] args) {

        SpringApplication.run(SwapiApplication.class, args);

        log.info("Started SWAPI main method");
    }

}
