package com.bix.imageprocessor.domain.image.model;

import java.math.BigDecimal;

public record ImageTransform(
        Long id,
        byte[] image,
        BigDecimal resizeRatio,
        Integer sepiaIntensity,
        Boolean grayscale,
        Boolean invertColors
) implements
        SepiaImageTransformParams,
        InvertColorsImageTransformParams,
        ResizeImageTransformParams,
        GrayscaleImageTransformParams {

    @SuppressWarnings("unchecked")
    public <T extends ImageTransformParams> T asParams() {
        return (T) this;
    }
}
