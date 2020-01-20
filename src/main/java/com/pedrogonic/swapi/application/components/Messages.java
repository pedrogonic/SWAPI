package com.pedrogonic.swapi.application.components;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;


@Component
public class Messages {

  private final MessageSourceAccessor accessor;

  public Messages(final MessageSource messageSource) {
    this.accessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
  }

  private String get(final String code) {
    return accessor.getMessage(code);
  }

  public String getErrorDuplicateKey(final String entity, final String key, final String... values) {
    return MessageFormat.format(get("error." + entity + ".dup.key." + key), values);
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
