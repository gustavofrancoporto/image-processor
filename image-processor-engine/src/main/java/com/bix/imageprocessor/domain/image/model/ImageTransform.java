package com.bix.imageprocessor.domain.image.model;

public record ImageTransform(
        Long imageTransformId,
        byte[] image,
        ImageTransformParams params) {
}
