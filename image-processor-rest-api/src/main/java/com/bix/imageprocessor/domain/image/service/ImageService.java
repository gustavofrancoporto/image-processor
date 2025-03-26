package com.bix.imageprocessor.domain.image.service;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.web.dto.image.ImageDto;

public interface ImageService {
    Image save(ImageDto imageDto);
}
