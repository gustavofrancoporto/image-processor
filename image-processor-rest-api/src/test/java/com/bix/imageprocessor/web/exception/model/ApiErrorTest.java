package com.bix.imageprocessor.web.exception.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@ExtendWith(MockitoExtension.class)
public class ApiErrorTest {

    @Mock List<ApiValidationError> validationErrors;

    @Test
    void testMessageConstructor() {
        var message = "Custom error message";
        var apiError = new ApiError(message);

        assertThat(apiError.getTimestamp()).isCloseTo(now(), within(5, SECONDS));
        assertThat(apiError.getMessage()).isEqualTo(message);
        assertThat(apiError.getValidationErrors()).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testMessageConstructorWithDefaultMessage(String message) {
        var apiError = new ApiError(message);

        assertThat(apiError.getTimestamp()).isCloseTo(now(), within(5, SECONDS));
        assertThat(apiError.getMessage()).isEqualTo("Unexpected error");
    }

    @Test
    void testMessageAndValidationErrorsConstructor() {
        var message = "Validation error message";
        var apiError = new ApiError(message, validationErrors);

        assertThat(apiError.getTimestamp()).isCloseTo(now(), within(5, SECONDS));
        assertThat(apiError.getMessage()).isEqualTo(message);
        assertThat(apiError.getValidationErrors()).isEqualTo(validationErrors);
    }
}
