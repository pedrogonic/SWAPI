package com.pedrogonic.swapi.application.exception;

public class PlanetNotFoundException extends EntityNotFoundException {

    public PlanetNotFoundException(String message) {
        super(message);
    }
}
