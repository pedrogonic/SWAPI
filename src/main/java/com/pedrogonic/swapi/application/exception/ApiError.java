package com.pedrogonic.swapi.application.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;


/**
 * Based on RFC7807 reference: https://tools.ietf.org/html/rfc7807
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiError {

  private String type;
  private String title;
  private int status;
  private String detail;
  private String instance;
  private String message;
  private List<String> errors;

  static Builder builder() {
    return new Builder();
  }

  static Builder builder(HttpStatus httpStatus) {
    return new Builder(httpStatus);
  }

  ResponseEntity<Object> toResponseEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);
    return new ResponseEntity<>(this, headers, HttpStatus.valueOf(this.status));
  }

  public static final class Builder {

    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private String message;
    private List<String> errors;

    private Builder() {
      this.type = "about:blank";
    }

    private Builder(HttpStatus httpStatus) {
      this.type = "https://httpstatuses.com/" + httpStatus.value();
      this.title = httpStatus.getReasonPhrase();
      this.status = httpStatus.value();
    }

    Builder withType(String type) {
      this.type = type;
      return this;
    }

    Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    Builder withInstance(String instance) {
      this.instance = instance;
      return this;
    }

    Builder withStatus(HttpStatus httpStatus) {
      status = httpStatus.value();
      return this;
    }

    Builder withDetail(String detail) {
      this.detail = detail;
      return this;
    }

    Builder withMessage(String message) {
      this.message = message;
      return this;
    }

    Builder withErrors(List<String> errors) {
      this.errors = errors;
      return this;
    }

    Builder withErrors(String error) {
      errors = Collections.singletonList(error);
      return this;
    }

    ApiError build() {
      return new ApiError(type, title, status, detail, instance, message, errors);
    }
  }
}
