package com.pedrogonic.swapi.application.exception;

public class PlanetNotFoundException extends ResourceNotFoundException {

    public PlanetNotFoundException(String message) {
        super(message);
    }
}
