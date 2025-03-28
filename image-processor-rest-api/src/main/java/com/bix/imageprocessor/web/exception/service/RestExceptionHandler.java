package com.bix.imageprocessor.web.exception.service;


import com.bix.imageprocessor.web.exception.model.ApiError;
import com.bix.imageprocessor.web.exception.model.RequestLimitReachedException;
import com.bix.imageprocessor.web.exception.model.UserAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final ApiValidationErrorFactory apiValidationErrorFactory;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        var apiError = new ApiError("Malformed JSON request", ex);
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiError> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        var message = ex.getParameterName() + " parameter is missing";
        var apiError = new ApiError(message, ex);

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

        var message = StringUtils.firstNonBlank(globalErrorMessage, "Invalid data.");

        var apiError = new ApiError(message, ex, validationErrors);

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(new ApiError("Internal server error", ex), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        var apiError = new ApiError("Access denied", ex);
        return new ResponseEntity<>(apiError, FORBIDDEN);
    }

    @ExceptionHandler(RequestLimitReachedException.class)
    protected ResponseEntity<ApiError> handleAccessDenied(RequestLimitReachedException ex) {
        var apiError = new ApiError(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        var apiError = new ApiError(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        var apiError = new ApiError(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }
}
