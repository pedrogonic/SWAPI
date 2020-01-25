package com.pedrogonic.swapi.application.exception;

public class ResourceNotFoundException extends Exception {
  public ResourceNotFoundException(final String message) {
    super(message);
  }
}
