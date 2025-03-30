package com.bix.imageprocessor.web.exception.service;


import com.bix.imageprocessor.web.exception.model.ApiError;
import com.bix.imageprocessor.web.exception.model.InternalApplicationException;
import com.bix.imageprocessor.web.exception.model.RequestLimitReachedException;
import com.bix.imageprocessor.web.exception.model.UserAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;
import static org.springframework.http.HttpHeaders.RETRY_AFTER;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final ApiValidationErrorFactory apiValidationErrorFactory;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("Malformed JSON request", ex);
        var apiError = new ApiError("Malformed JSON request");
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiError> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        var message = ex.getParameterName() + " parameter is missing";
        var apiError = new ApiError(message);

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        var validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this.apiValidationErrorFactory::create)
                .toList();

        var globalErrorMessage = ex.getBindingResult().getGlobalErrors().stream()
                .map(this.apiValidationErrorFactory::create)
                .collect(joining(", "));

        var message = firstNonBlank(globalErrorMessage, "Validation error");

        var apiError = new ApiError(message, validationErrors);

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Internal server error", ex);
        var apiError = new ApiError("Internal server error");
        return new ResponseEntity<>(apiError, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiError> handleAccessDenied() {
        var apiError = new ApiError("Access denied");
        return new ResponseEntity<>(apiError, FORBIDDEN);
    }

    @ExceptionHandler(RequestLimitReachedException.class)
    protected ResponseEntity<ApiError> handleRequestLimitReachedException(RequestLimitReachedException ex) {

        var apiError = new ApiError("Request limit reached");
        var headers = new HttpHeaders();
        headers.set(RETRY_AFTER, String.valueOf(ex.getRetryAfter()));

        return new ResponseEntity<>(apiError, headers, TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ApiError> handleUserAlreadyExists() {
        var apiError = new ApiError("User already exists");
        return new ResponseEntity<>(apiError, CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        var apiError = new ApiError(ex.getMessage());
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler(InternalApplicationException.class)
    protected ResponseEntity<ApiError> handleInternalError(InternalApplicationException ex) {
        log.error("Internal server error", ex);
        var apiError = new ApiError("Internal server error");
        return new ResponseEntity<>(apiError, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex) {
        var apiError = new ApiError(ex.getMessage());
        return new ResponseEntity<>(apiError, UNAUTHORIZED);
    }
}
