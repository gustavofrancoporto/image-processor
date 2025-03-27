package com.bix.imageprocessor.web.exception.service;

import com.bix.imageprocessor.web.exception.model.ApiValidationError;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public interface ApiValidationErrorFactory {
    ApiValidationError create(FieldError fieldError);

    String create(ObjectError objectError);
}
