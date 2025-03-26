package com.bix.imageprocessor.web.dto.image;

import java.math.BigDecimal;
import java.util.List;

public record ImageTransformParamsDto(
        BigDecimal resizePercentage,
        List<String> filters
) {
}
