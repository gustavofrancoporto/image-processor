package com.bix.imageprocessor.domain.image.service;


public interface ImageTransformQueueSender {
    void send(Long imageTransformId);
}
