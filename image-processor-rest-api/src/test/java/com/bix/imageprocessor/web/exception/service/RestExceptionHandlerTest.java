package com.bix.imageprocessor.web.exception.service;

import com.bix.imageprocessor.web.exception.model.ApiValidationError;
import com.bix.imageprocessor.web.exception.model.InternalApplicationException;
import com.bix.imageprocessor.web.exception.model.RequestLimitReachedException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.RETRY_AFTER;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class RestExceptionHandlerTest {

    @Mock ApiValidationErrorFactory apiValidationErrorFactory;
    @InjectMocks RestExceptionHandler restExceptionHandler;

    @Mock HttpMessageNotReadableException httpMessageNotReadableException;
    @Mock MissingServletRequestParameterException missingParamException;
    @Mock DataIntegrityViolationException dataIntegrityViolationException;
    @Mock EntityNotFoundException entityNotFoundException;
    @Mock InternalApplicationException internalApplicationException;
    @Mock BadCredentialsException badCredentialsException;
    @Mock MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock RequestLimitReachedException requestLimitReachedException;

    @Mock FieldError fieldError1, fieldError2;
    @Mock ObjectError objectError;
    @Mock BindingResult bindingResult;
    @Mock ApiValidationError apiValidationError1, apiValidationError2;

    @Test
    void shouldHandleHttpMessageNotReadable() {
        var response = restExceptionHandler.handleHttpMessageNotReadable(httpMessageNotReadableException);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Malformed JSON request");
    }

    @Test
    void shouldHandleMissingServletRequestParameter() {
        var paramName = "imageId";
        when(missingParamException.getParameterName()).thenReturn(paramName);

        var response = restExceptionHandler.handleMissingServletRequestParameter(missingParamException);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(paramName + " parameter is missing");
    }

    @Test
    void shouldHandleDataIntegrityViolation() {
        var response = restExceptionHandler.handleDataIntegrityViolation(dataIntegrityViolationException);

        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Internal server error");
    }

    @Test
    void shouldHandleAccessDenied() {
        var response = restExceptionHandler.handleAccessDenied();

        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Access denied");
    }

    @Test
    void shouldHandleRequestLimitReached() {

        long retryAfter = 6000;
        when(requestLimitReachedException.getRetryAfter()).thenReturn(retryAfter);

        var response = restExceptionHandler.handleRequestLimitReachedException(requestLimitReachedException);

        assertThat(response.getStatusCode()).isEqualTo(TOO_MANY_REQUESTS);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Request limit reached");
        assertThat(response.getHeaders()).containsOnly(Map.entry(RETRY_AFTER, List.of(String.valueOf(retryAfter))));
    }

    @Test
    void shouldHandleUserAlreadyExists() {

        var response = restExceptionHandler.handleUserAlreadyExists();

        assertThat(response.getStatusCode()).isEqualTo(CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User already exists");
    }

    @Test
    void shouldHandleEntityNotFound() {
        var message = "Entity not found";
        when(entityNotFoundException.getMessage()).thenReturn(message);

        var response = restExceptionHandler.handleEntityNotFound(entityNotFoundException);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(message);
    }

    @Test
    void shouldHandleInternalApplicationException() {
        var response = restExceptionHandler.handleInternalError(internalApplicationException);

        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Internal server error");
    }

    @Test
    void shouldHandleBadCredentials() {

        var message = "Invalid username or password";
        when(badCredentialsException.getMessage()).thenReturn(message);

        var response = restExceptionHandler.handleBadCredentialsException(badCredentialsException);

        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(message);
    }

    @Test
    void shouldHandleMethodArgumentNotValid() {

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        when(apiValidationErrorFactory.create(fieldError1)).thenReturn(apiValidationError1);
        when(apiValidationErrorFactory.create(fieldError2)).thenReturn(apiValidationError2);

        var response = restExceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Validation error");
        assertThat(response.getBody().getValidationErrors()).containsExactly(apiValidationError1, apiValidationError2);
    }
}
