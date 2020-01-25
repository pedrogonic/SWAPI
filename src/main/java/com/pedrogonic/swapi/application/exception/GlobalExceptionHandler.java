package com.pedrogonic.swapi.application.exception;

import com.pedrogonic.swapi.application.components.Messages;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private Messages messages;

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex) {
    log.info(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    //
    final String error = ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();

    final ApiError apiError =
        ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(error).build();
    return apiError.toResponseEntity();
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex) {
    log.info(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    //
    List<String> userMessage = new ArrayList<>();
    List<String> violationsKeyMap = new ArrayList<>();

    for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      userMessage.add(violation.getMessage());
      violationsKeyMap.add(violation.getPropertyPath() + " - invalid value: " + violation.getInvalidValue());
    }

    final ApiError apiError =
        ApiError.builder(httpStatus).withDetail("A set of constraint violations was reported during a validation.")
            .withMessage(StringUtils.join(userMessage, " | ")).withErrors(violationsKeyMap).build();

    return apiError.toResponseEntity();
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    //
    final ApiError apiError =
            ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(ex.getMessage())
                    .build();
    return apiError.toResponseEntity();
  }

  @ExceptionHandler({SwapiUnreachableException.class})
  public ResponseEntity<Object> handleApiCallException(final SwapiUnreachableException ex) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;
    //
    final ApiError apiError =
            ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(ex.getMessage())
                    .build();
    return apiError.toResponseEntity();
  }

  // TODO: not being picked up properly
  @ExceptionHandler({DuplicateKeyException.class})
  public ResponseEntity<Object> handleDuplicateKeyException(final DuplicateKeyException ex) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.CONFLICT;

    String errorMsg = ex.getMostSpecificCause().getMessage();
    String entity = StringUtils.substringBetween(errorMsg, "collection: swapi.", " index:");
    String key = StringUtils.substringBetween(errorMsg, "index: ", " dup key:");

    String errorMessage = messages.getErrorDuplicateKey(entity, key);
//    String errorMessage = "This is a hardcoded error!";


    final ApiError apiError =
        ApiError.builder(httpStatus).withDetail("Some unique fields have duplicated stored values.")
            .withMessage(errorMessage).build();

    return apiError.toResponseEntity();
  }

  private ResponseEntity<Object> handleMethodArgumentNotValidAndBindException(final List<FieldError> fieldErrors, final List<ObjectError> objectErrors,
                                                                              final String localizedMessage,
                                                                              final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);

    //
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : fieldErrors) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : objectErrors) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    final ApiError apiError =
            ApiError.builder().withStatus(httpStatus).withDetail(localizedMessage).withErrors(errors).build();
    return apiError.toResponseEntity();
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    return handleMethodArgumentNotValidAndBindException(ex.getBindingResult().getFieldErrors(), ex.getBindingResult().getGlobalErrors(),
                                                                ex.getLocalizedMessage(), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
                                                       final HttpStatus status, final WebRequest request) {
    return handleMethodArgumentNotValidAndBindException(ex.getBindingResult().getFieldErrors(), ex.getBindingResult().getGlobalErrors(),
            ex.getLocalizedMessage(), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
                                                      final HttpStatus status, final WebRequest request) {
    log.info(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    //
    final String error =
        ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

    final ApiError apiError =
        ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(error).build();

    return apiError.toResponseEntity();
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                   final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    //
    final String error = ex.getRequestPartName() + " part is missing";
    final ApiError apiError =
        ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(error).build();
    return apiError.toResponseEntity();
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
          final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
          final WebRequest request) {
    log.info(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    //
    final String error = ex.getParameterName() + " parameter is missing";
    final ApiError apiError =
        ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(error).build();
    return apiError.toResponseEntity();
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
                                                                       final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;

    //
    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");
    Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

    final ApiError apiError =
        ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(builder.toString())
            .build();
    return apiError.toResponseEntity();
  }

  // 415
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
                                                                   final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    //
    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));

    final ApiError apiError = ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage())
        .withErrors(builder.substring(0, builder.length() - 2)).build();

    return apiError.toResponseEntity();
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
                                                                 final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    //
    final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

    final ApiError apiError =
        ApiError.builder().withStatus(httpStatus).withDetail(ex.getLocalizedMessage()).withErrors(error).build();
    return apiError.toResponseEntity();
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(final HttpMediaTypeNotAcceptableException ex,
                                                                    final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(final MissingPathVariableException ex,
                                                             final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex,
                                                                        final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(final ConversionNotSupportedException ex,
                                                                final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(final HttpMessageNotWritableException ex,
                                                                final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleAsyncRequestTimeoutException(final AsyncRequestTimeoutException ex,
                                                                      final HttpHeaders headers, final HttpStatus status, final WebRequest webRequest) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
    //
    final ApiError apiError =
        ApiError.builder(httpStatus).withDetail("Request timed out.").withMessage(messages.getErrorHttpRequestTimeout())
            .build();

    return apiError.toResponseEntity();
  }

  @Override
  @ExceptionHandler({Exception.class})
  protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body,
                                                           final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.error(ex.getMessage(), ex);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    //
    final ApiError apiError = ApiError.builder(httpStatus)
        .withDetail("The server encountered an unexpected condition that prevented it from fulfilling the request.")
        .withMessage(messages.getErrorHttpInternalServer()).build();

    return apiError.toResponseEntity();
  }
}
