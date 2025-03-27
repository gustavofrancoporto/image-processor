package com.bix.imageprocessor.domain.image.model;

import java.util.List;

public record ImageTransformParams(
        Float resizeRatio,
        List<String> filters
) {
}
