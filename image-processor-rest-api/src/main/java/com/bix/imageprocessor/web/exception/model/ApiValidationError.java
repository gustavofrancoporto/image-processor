package com.bix.imageprocessor.web.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String code;
    private String message;
}
