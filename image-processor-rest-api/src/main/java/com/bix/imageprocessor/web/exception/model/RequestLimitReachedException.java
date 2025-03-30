package com.bix.imageprocessor.web.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestLimitReachedException extends RuntimeException {

    private final long retryAfter;
}
