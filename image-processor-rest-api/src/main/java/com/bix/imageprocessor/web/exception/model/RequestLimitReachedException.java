package com.bix.imageprocessor.web.exception.model;

public class RequestLimitReachedException extends RuntimeException {

    public RequestLimitReachedException() {
        super("Request limit reached");
    }
}
