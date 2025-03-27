package com.bix.imageprocessor.web.exception.service.impl;

import com.bix.imageprocessor.web.exception.model.ApiValidationError;
import com.bix.imageprocessor.web.exception.service.ApiValidationErrorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Locale;

import static org.apache.commons.lang3.ArrayUtils.*;

@Service
@RequiredArgsConstructor
public class DefaultApiValidationErrorFactory implements ApiValidationErrorFactory {

    private final MessageSource messageSource;

    @Override
    public ApiValidationError create(FieldError fieldError) {

        var arguments = getArguments(fieldError);
        var messageSourceResolvable = new DefaultMessageSourceResolvable(nullToEmpty(fieldError.getCodes()), arguments);
        var message = messageSource.getMessage(messageSourceResolvable, Locale.getDefault());

        return new ApiValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getCode(),
                message
        );
    }

    @Override
    public String create(ObjectError objectError) {

        var messageSourceResolvable = new DefaultMessageSourceResolvable(nullToEmpty(objectError.getCodes()));
        return messageSource.getMessage(messageSourceResolvable, Locale.getDefault());
    }

    private Object[] getArguments(FieldError fieldError) {

        if (fieldError.getArguments() == null || fieldError.getArguments().length == 0) {
            return EMPTY_OBJECT_ARRAY;
        }

        var arguments = remove(fieldError.getArguments(), 0);
        reverse(arguments);

        return arguments;
    }
}
