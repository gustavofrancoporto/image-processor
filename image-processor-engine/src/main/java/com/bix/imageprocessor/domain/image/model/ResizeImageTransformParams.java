package com.bix.imageprocessor.domain.image.model;

import java.math.BigDecimal;

public interface ResizeImageTransformParams extends ImageTransformParams {

    BigDecimal resizeRatio();
}
