package com.pedrogonic.swapi.application.components;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;


@Component
public class Messages {

  private final MessageSourceAccessor accessor;

  public Messages(final MessageSource messageSource) {
    this.accessor = new MessageSourceAccessor(messageSource, Locale.US);
  }

  private String get(final String code) {
    return accessor.getMessage(code);
  }

  public String getErrorDuplicateKeys() {
    return get("error.constraint.dup.key");
  }

  public String getErrorDuplicateKey(final String entity, final String key, final String id) {
    return MessageFormat.format(get("error." + entity + ".dup.key." + key), id);
  }

  public String getValidationErrorMessage(String prop) {
      return get(prop.replaceAll("[{}]", ""));
  }

  public String getErrorHttpInternalServer() {
    return get("error.http.internal.server");
  }

  public String getErrorHttpRequestTimeout() {
    return get("error.http.request.timeout");
  }

  public String getErrorPlanetNotFoundById(final String id) {
    return MessageFormat.format(get("error.planet.not.found.by.id"), id);
  }

  public String getErrorPlanetNotFoundInSwapi(final String id) {
    return MessageFormat.format(get("error.planet.not.found.in.swapi"), id);
  }

  public String getErrorSwapiUnreachable(final String status) {
    return MessageFormat.format(get("error.swapi.unreachable"), status);
  }


}
