package com.bix.imageprocessor.web.dto.image;

import java.util.List;

public record ImageTransformParamsDto(
        Float resizeRatio,
        List<String> filters
) {
}
