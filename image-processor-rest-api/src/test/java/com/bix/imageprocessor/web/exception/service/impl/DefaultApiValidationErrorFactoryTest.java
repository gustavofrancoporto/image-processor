package com.bix.imageprocessor.web.exception.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Locale;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultApiValidationErrorFactoryTest {

    @Mock MessageSource messageSource;

    @InjectMocks DefaultApiValidationErrorFactory subject;

    static Stream<Arguments> fieldErrorArgumentsMethodSource() {
        return Stream.of(
                Arguments.of(null, new String[]{}),
                Arguments.of(new String[]{}, new String[]{}),
                Arguments.of(new String[]{"a"}, new String[]{}),
                Arguments.of(new String[]{"a", "b", "c"}, new String[]{"c", "b"})
        );
    }

    @ParameterizedTest
    @MethodSource("fieldErrorArgumentsMethodSource")
    void shouldCreateApiValidationErrorFromFieldError(String[] originalArguments, String[] usedArguments) {

        var codes = new String[]{"error.code"};
        var errorMessage = "Field error message";

        var fieldError = new FieldError("objectName", "fieldName", "rejectedValue", true, codes, originalArguments, "Default message");
        var messageSourceResolvable = new DefaultMessageSourceResolvable(codes, usedArguments);

        when(messageSource.getMessage(messageSourceResolvable, Locale.getDefault()))
                .thenReturn(errorMessage);

        var apiValidationError = subject.create(fieldError);

        assertThat(apiValidationError.getObject()).isEqualTo("objectName");
        assertThat(apiValidationError.getField()).isEqualTo("fieldName");
        assertThat(apiValidationError.getRejectedValue()).isEqualTo("rejectedValue");
        assertThat(apiValidationError.getCode()).isEqualTo("error.code");
        assertThat(apiValidationError.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void shouldCreateErrorMessageFromObjectError() {

        var codes = new String[]{"error.code"};
        var errorMessage = "Field error message";

        var objectError = new ObjectError("objectName", codes, null, "Default message");
        var messageSourceResolvable = new DefaultMessageSourceResolvable(codes);

        when(messageSource.getMessage(messageSourceResolvable, Locale.getDefault()))
                .thenReturn(errorMessage);

        var errorMessageCreated = subject.create(objectError);

        assertThat(errorMessageCreated).isEqualTo(errorMessage);
    }
}
