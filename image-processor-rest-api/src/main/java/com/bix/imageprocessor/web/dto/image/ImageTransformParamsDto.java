package com.bix.imageprocessor.web.dto.image;

import com.bix.imageprocessor.web.validation.ImageTransformParamsRequired;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@ImageTransformParamsRequired
public record ImageTransformParamsDto(
        @DecimalMin("0.1")
        @DecimalMax("1.0")
        BigDecimal resizeRatio,

        @Range(min = 1, max = 100)
        Integer sepiaIntensity,

        Boolean grayscale,

        Boolean invertColors
) {
}
