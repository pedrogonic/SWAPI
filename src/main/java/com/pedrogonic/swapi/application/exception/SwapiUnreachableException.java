package com.pedrogonic.swapi.application.exception;

public class SwapiUnreachableException extends EntityNotFoundException {

    public SwapiUnreachableException(String message) {
        super(message);
    }

}
