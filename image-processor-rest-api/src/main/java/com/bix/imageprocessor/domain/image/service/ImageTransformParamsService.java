package com.bix.imageprocessor.domain.image.service;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;

public interface ImageTransformParamsService {
    ImageTransformParams save(ImageTransformParamsDto imageTransformIdParamsDto);
}
