package com.pedrogonic.swapi.application.exception;

public class EntityNotFoundException extends Exception {

  public EntityNotFoundException(final String message) {
    super(message);
  }
}
