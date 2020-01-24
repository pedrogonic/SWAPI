package com.pedrogonic.swapi.application.exception;

public class SwapiUnreachableException extends EntityNotFoundException {
//  TODO do not extend EntityNotFoundException. THROW 500 or 503 if unreachable
    public SwapiUnreachableException(String message) {
        super(message);
    }

}
