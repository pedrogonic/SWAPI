package com.pedrogonic.swapi.application.exception;

public class EntityNotFoundException extends Exception {
//TODO change to resource not found
  public EntityNotFoundException(final String message) {
    super(message);
  }
}
