package com.bix.imageprocessor.web.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

@Data
@JsonInclude(NON_NULL)
public class ApiError {

    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private List<ApiValidationError> validationErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(String message, Throwable ex) {
        this();
        this.message = defaultIfBlank(message, "Unexpected error");
    }

    public ApiError(String message, Throwable exception, List<ApiValidationError> validationErrors) {
        this(message, exception);
        this.validationErrors = validationErrors;
    }
}
