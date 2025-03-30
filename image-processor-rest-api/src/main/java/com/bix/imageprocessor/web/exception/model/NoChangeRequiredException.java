package com.bix.imageprocessor.web.exception.model;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@ResponseStatus(code = NO_CONTENT)
public class NoChangeRequiredException extends RuntimeException {
}
